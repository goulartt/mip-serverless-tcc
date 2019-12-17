const db = require('../database')

module.exports.findAll = async () => {
    let fields = await db.from('field')
        .select()

    return Object.values(JSON.parse(JSON.stringify(fields)))
}


module.exports.checkEntityExists = async (field) => {
    return await db
        .from('field')
        .select('id')
        .where({ name: field.name, city_id: field.city_id, farmer_id: field.farmer_id, location: field.location })
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

    for (let i = 0; i < supervisors.length; i++) {
        await db('field_supervisors').insert({ field_id: field.id, supervisors_id: supervisors[i] })
    }
}

module.exports.checkUser = async (id) => {
    const createdId = await db
        .from('field')
        .select('created_by_id')
        .where({ id: id })

    if (createdId.length == 0)
        throw new Error('Field não existente no banco de dados')

    return createdId[0].created_by_id == field.userId
}

module.exports.delete = async (id) => {
    await db('field_supervisors')
        .where({ field_id: id })
        .del()

    await db('field')
        .where({ id: id })
        .del()
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