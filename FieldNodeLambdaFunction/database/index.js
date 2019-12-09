const dbConfig = require('../config/database')

module.exports = require('knex')(dbConfig)