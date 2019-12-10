'use strict';

const service = require('./service')



module.exports = {
  create: async (event) => {
    //let data = JSON.parse(event.Records[0].body)
    let data = event

    const response = await service.create(data)

    return response
  }
}