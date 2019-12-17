const serverless = require('serverless-http')
const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const cors = require('cors')

app.use(cors())
app.use(bodyParser.json())

require('../controller/fieldController')(app)

module.exports.handler = serverless(app)