const db = require('../database')

module.exports.findAll = async () => {
    fields = await db
        .select('field.*', 'city.name as city_name', 'city.state as city_state', 'farmer.name as farmer_name')
        .from('field')
        .leftJoin('city', { 'city.id': 'field.city_id' })
        .leftJoin('farmer', { 'farmer.id': 'field.farmer_id' })

    fields = Object.values(JSON.parse(JSON.stringify(fields)))

    return Promise.all(fields.map(async (field) => {

        let supervisors = await db
            .select('id', 'name')
            .from('supervisor')
            .join('field_supervisors', { 'field_supervisors.supervisors_id': 'supervisor.id' })
            .where('field_supervisors.field_id', '=', field.id)

        supervisors = Object.values(JSON.parse(JSON.stringify(supervisors)))

        field = {
            id: field.id,
            name: field.name,
            position: field.position,
            created_at: field.created_at,
            last_modified: field.last_modified,
            created_by_id: field.created_by_id,
            modified_by_id: field.modified_by_id,
            city: {
                id: field.city_id,
                name: field.city_name,
                state: field.city_state
            },
            farmer: {
                id: field.farmer_id,
                name: field.farmer_name
            },
            supervisors
        }
        return field
    }))

}


module.exports.checkEntityExists = async (field) => {
    return await db
        .from('field')
        .select('id')
        .where({ name: field.name, city_id: field.city_id, farmer_id: field.farmer_id, location: field.location })
}

module.exports.checkEntityExistsById = async (id) => {
    return await db
        .from('field')
        .select('id')
        .where({ id })
}



module.exports.allowedInCity = async (supervisors, citySelected) => {

    let regionsId = await db
        .from('supervisor')
        .select('region_id')
        .whereIn('id', supervisors)

    regionsId = Object.values(JSON.parse(JSON.stringify(regionsId)))
    regionsId = regionsId.map(({ region_id }) => region_id)

    let res = await db
        .from('region_cities')
        .select('cities_id')
        .whereIn('region_id', regionsId)

    res = Object.values(JSON.parse(JSON.stringify(res)))

    for (let i = 0; i < res.length; i++) {
        if (res[i].cities_id === citySelected)
            return true
    }

    return false

}

module.exports.insertField = async (field, supervisors) => {

    const res = await db('field')
        .insert(field)

    field.id = res[0]

}

module.exports.checkUser = async (userId, fieldId) => {
    const createdId = await db
        .from('field')
        .select('created_by_id')
        .where({ id: fieldId })

    if (!createdId)
        throw new Error('Field não existente no banco de dados')

    return createdId[0].created_by_id == userId
}

module.exports.delete = async (id) => {

    deleteSupervisors(id)

    await db('field')
        .where({ id: id })
        .del()
}


module.exports.deleteSupervisors = async (id) => {
    await db('field_supervisors')
        .where({ field_id: id })
        .del()

}

module.exports.insertSupervisors = async (field, supervisors) => {
    for (let i = 0; i < supervisors.length; i++) {
        await db('field_supervisors').insert({ field_id: field.id, supervisors_id: supervisors[i] })
    }

}


module.exports.find = async (id) => {

    let res = await db
        .from('field')
        .select()
        .where({ id: id })

    if (res.length > 0)
        return Object.values(JSON.parse(JSON.stringify(res)))[0]

    return null
}

module.exports.update = async (field, supervisors) => {

    deleteSupervisors(id)
    insertSupervisors(field, supervisors)

    let res = knex('field')
        .where({ id: field.id })
        .update({ field })

    return res
}

