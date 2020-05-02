const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const helmet = require('helmet')
const cors = require('cors')

app.use(cors())
app.use(helmet())
app.use(bodyParser.urlencoded({ extended: true }))
app.use(bodyParser.json())



module.exports = app
