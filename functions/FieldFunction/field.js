'use strict'

const service = require('./service')

module.exports = {

  create: async (event) => {
    return await service.create(event)
  },

  delete: async (event) => {
    return await service.delete(event)
  }
  
}