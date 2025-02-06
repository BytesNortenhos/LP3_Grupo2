const router = require('express').Router();
const { obterGames, obterTickets } = require('../functions/opo.js');

router.get('/', async (req, res) => {
    let stateLogin = req.session.stateLogin;
    let errorMsg = req.session.errorMsg;
    delete req.session.stateLogin;
    delete req.session.errorMsg;

    //-> req.session.passport.user
    //-> req.session.passport.user.id
    //-> req.session.passport.user.groupId
    //-> req.session.passport.user.name
    //-> req.session.passport.user.email
    //-> req.session.passport.user.active

    let resultGames = await obterGames();
    let resultTickets = await obterTickets(req.session.passport.user.id);
    if(stateLogin === 0) return res.render('home.ejs', { alert: true, type: 'success', message: 'Bilhete comprado!', games: resultGames, tickets: resultTickets });
    if(stateLogin === 1) return res.render('home.ejs', { alert: true, type: 'success', message: 'Palavra-passe alterada!', games: resultGames, tickets: resultTickets });
    if(stateLogin === 2) return res.render('home.ejs', { alert: true, type: 'error', message: errorMsg, games: resultGames, tickets: resultTickets });

    res.render('home.ejs', { alert: false, games: resultGames, tickets: resultTickets });
});

module.exports = router;