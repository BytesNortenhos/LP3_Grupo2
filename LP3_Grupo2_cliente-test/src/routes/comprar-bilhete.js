const router = require('express').Router();
const { comprarBilhete } = require('../functions/opo.js');

router.get('/:idGame', async (req, res) => {
    let idGame = req.params.idGame;

    let result = await comprarBilhete(idGame, req.session.passport.user.id);
    switch(result.stateRequest) {
        case 1:
            req.session.stateLogin = 0;
            res.redirect('/');
            break;
        case 99:
            req.session.stateLogin = 2;
            req.session.errorMsg = result.errorMsg;
            res.redirect('/');
            break;
    }
});

module.exports = router