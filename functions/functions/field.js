const serverless = require('serverless-http')
const app = require('../app')


require('../controller/field.controller')(app)

module.exports.handler = serverless(app)