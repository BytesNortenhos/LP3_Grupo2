const router = require('express').Router();
const { alterarPassword } = require('../functions/opo.js');

router.post('/', async (req, res) => {
    const newPassword = req.body.inpValue;
    let result = await alterarPassword(req.session.passport.user.id, newPassword);

    switch(result.stateLogin) {
        case 1:
            req.session.stateLogin = 1;
            res.redirect('/');
            break;
        case 99:
            req.session.stateLogin = 2;
            req.session.errorMsg = result.errorMsg;
            res.redirect('/');
            break;
    }
});

module.exports = router;