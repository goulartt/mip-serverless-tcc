'use strict'

const serverless = require('serverless-http')
const app = require('../app')


require('../controller/fieldController')(app)

module.exports.handler = serverless(app)