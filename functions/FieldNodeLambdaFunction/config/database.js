module.exports = {
    client: 'mysql',
    connection: {
        host: process.env.RDS_URL,
        user: process.env.RDS_USER,
        password: process.env.RDS_PASSWORD,
        database: 'mip'
    }
}