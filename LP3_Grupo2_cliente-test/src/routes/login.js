const router = require('express').Router();
const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;
const { login } = require('../functions/opo.js');

router.get('/', (req, res) => {
    let stateLogin = req.session.stateLogin;
    let errorMsg = req.session.errorMsg;
    delete req.session.stateLogin;
    delete req.session.errorMsg;

    if(stateLogin === 1) return res.render('login.ejs', {alert: true, type: 'success', message: 'Conta criada com sucesso, por favor faça login!'});
    if(stateLogin === 99) return res.render('login.ejs', {alert: true, type: 'error', message: errorMsg});
    res.render('login.ejs', { alert: false });
});

router.post('/', (req, res, next) => {
    passport.authenticate('local', function(err, user, info) {
        if (err) return next(err);
        
        if (!user) {
            req.session.stateLogin = info.stateLogin;
            req.session.errorMsg = info.errorMsg;
            return res.redirect('/login');
        }

        req.logIn(user, function(err) {
            if (err) return next(err);
            req.session.logged = true;

            return res.redirect('/');
        });
    })(req, res, next);
});

/* PassportJS Local */
passport.use('local', new LocalStrategy({
    passReqToCallback: true, 
    usernameField: 'email',
    passwordField: 'password'
}, async function(req, username, password, done) {
    let email = username.toLowerCase();

    let result = await login(email, password);

    switch(result.stateLogin) {
        case 1:
            return done(null, {
                id: result.data.Client[0].Id,
                groupId: result.data.Client[0].GroupId,
                name: result.data.Client[0].Name,
                email: result.data.Client[0].Email,
                active: result.data.Client[0].Active,
            }, {});
            break;
        case 99:
            return done(null, false, { stateLogin: result.stateLogin, errorMsg: result.errorMsg });
            break;
    }
}));

/* PassportJS */
passport.serializeUser(function(user, cb) {
    cb(null, user);
});

passport.deserializeUser(function(obj, cb) {
    cb(null, obj);
});

module.exports = router;