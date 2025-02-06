const router = require('express').Router();
const { eliminarConta } = require('../functions/opo.js');

router.get('/', async (req, res) => {
    let result = await eliminarConta(req.session.passport.user.id);

    switch(result.stateRequest) {
        case 1:
            res.redirect('/logout');
            break;
        case 99:
            req.session.stateLogin = 2;
            req.session.errorMsg = result.errorMsg;
            res.redirect('/');
            break;
    }
});

module.exports = router