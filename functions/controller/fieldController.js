const express = require('express')
const router = express.Router()
const fieldService = require('../service/fieldService')



router.post('/', async (req, res) => {

    let data = req.body
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
        const exists = await fieldService.checkEntityExists(field)

        if (exists.length > 0)
            return res.status(409).send({ message: 'Essa entidade já existe no banco de dados' })

        const supervisorsAllowed = await fieldService.allowedInCity(supervisors, field.city_id)

        if (!supervisorsAllowed)
            return res.status(405).send({ message: 'O supervisor não tem permissão para essa cidade' })

        await fieldService.insertField(field, supervisors)

    } catch (e) {
        return res.status(500).send({ error: e })
    }
    return res.status(201).send(field)

})



router.get('/', async (req, res) => {

    let fields = await fieldService.findAll()

    return res.status(200).send(fields)
})

router.get('/:id', async (req, res) => {

    const id = req.params.id

    let field = await fieldService.find(id)

    if (field)
        return res.status(200).send(field)

    return res.status(204).send({ message: `Field de id ${id} não encontrado` })
})

router.delete('/:id', async (req, res) => {

    const id = req.params.id

    try {
        const isSameUser = await fieldService.checkUser(id)

        if (!isSameUser)
            return res.status(405).send({ message: 'O usuário não tem permissão para deletar um recurso que não foi criado pelo mesmo' })

        await fieldService.delete(id)

        return res.status(204).send({ message: `Field id ${id} deletado` })
    } catch (e) {
        console.log(e)
        if (e.toString().indexOf('Field não existente') != -1)
            return res.status(404).send({ error: e })

        return res.status(500).send({ error: e })
    }
})

module.exports = app => app.use('/field', router);