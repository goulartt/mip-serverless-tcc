const db = require('../database')

const sendRes = (status, body) => {
    let response = {
        statusCode: status,
        headers: {
            "Content-Type": "text/json"
        },
        body: body
    };
    return response
}

const checkEntityExists = async (field) => {
    return await db
        .from('field')
        .select('id')
        .where({ name: field.name, city_id: field.city_id, farmer_id: field.farmer_id, location: field.location })
}


const allowedInCity = async (supervisors, citySelected) => {

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

const insertField = async (field, supervisors) => {

    const res = await db('field')
        .insert(field)
        
    const id = res[0]
    
    for (let i = 0; i < supervisors.length; i++) {
        await db('field_supervisors').insert({ field_id: id, supervisors_id: supervisors[i] })
    }
}

exports.create = async (event) => {
    let data = event
    let time = new Date().getTime()

    const field = {
        created_at: time,
        last_modified: time,
        location: data.location,
        name: data.name,
        city_id: data.cityId,
        farmer_id: data.farmerId,
        modified_by_id: data.modifiedBy,
        created_by_id: data.createdBy,
    }

    console.log(field)
    
    try {
        const supervisors = data.supervisors
        const exists = await checkEntityExists(field)

        if (exists.length > 0)
            return sendRes(409, { message: 'Essa entidade já existe no banco de dados' })

        const supervisorsAllowed = await allowedInCity(supervisors, field.city_id)

        if (!supervisorsAllowed)
            return sendRes(405, { message: 'O supervisor não tem permissão para essa cidade' })

        await insertField(field, supervisors)

    } catch (e) {
        return sendRes(500, e)
    }

    return sendRes(200, field)
};
