const router = require('express').Router();
const { registar } = require('../functions/opo.js');
const { enviarEmail } = require('../functions/resend.js');

router.get('/', (req, res) => {
    let stateLogin = req.session.stateLogin;
    let errorMsg = req.session.errorMsg;
    delete req.session.stateLogin;
    delete req.session.errorMsg;

    if(stateLogin === 99) return res.render('register.ejs', {alert: true, type: 'error', message: errorMsg});
    res.render('register.ejs', { alert: false });
});

router.post('/', async (req, res) => {
    let username = req.body.username.toLowerCase();
    let email = req.body.email.toLowerCase();
    let password = req.body.password;

    let result = await registar(username, email, password);

    switch(result.stateLogin) {
        case 1:
            let emailResult = await enviarEmail(username, email, password);
            if(emailResult.status === 0) { console.log("> Erro ao enviar Email: " + emailResult.message); }
            req.session.stateLogin = 1;
            res.redirect('/login');
            break;
        case 99:
            console.log("aaaa")
            req.session.stateLogin = 99;
            req.session.errorMsg = result.errorMsg;
            res.redirect('/registar');
            break;
    }
});

module.exports = router;