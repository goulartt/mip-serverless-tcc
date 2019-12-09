'use strict';

const db = require('./database')

const sendRes = (status, body) => {
  var response = {
    statusCode: status,
    headers: {
      "Content-Type": "text/json"
    },
    body: body
  };
  return response;
}

module.exports = {
  create: async (event) => {
    let data = JSON.parse(event.Records[0].body)
    let time = new Date().getTime()

    console.log(data)

    const field = {
      created_at: time, 
      last_modified: time, 
      location: data.location, 
      name: data.name, 
      city_id: data.city.id, 
      farmer_id: data.farmer.id
    }

    console.log(field)

    await db('field')
      .insert(field)


    return sendRes(200, field)
  }
}