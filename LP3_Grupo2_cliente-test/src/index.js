require('dotenv').config();
const path = require('path');
const cookieParser = require('cookie-parser');
const favicon = require('serve-favicon');
const session = require('express-session');
const passport = require('passport');
const { checkLogged, checkNotLogged } = require('./functions/checkAuth.js');

const express = require('express');
const app = express();

app.use('/css', express.static(path.join(__dirname + '/styles/')));
app.use('/images', express.static(path.join(__dirname + '/images/')));

app.set('view engine', 'ejs');
app.set('views', [ path.join(__dirname, '/pages'), path.join(__dirname, '/components') ]);

app.use(cookieParser(process.env.WEBSITE_COOKIE_SECRET));
app.use(session({
    name: 'sessionData',
    secret: process.env.WEBSITE_SESSION_SECRET,
    resave: false,
    saveUninitialized: false,
    cookie: {
        maxAge: 86400000 * 7,
        httpOnly: true
    }
}));

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(favicon(path.join(__dirname + '/images/iconOlympic.png')));

app.use(passport.initialize());
app.use(passport.session());

//-> Rotas:
app.use('/login', checkNotLogged, require('./routes/login.js'));
app.use('/registar', checkNotLogged, require('./routes/register.js'));
app.use('/alterar-password', checkLogged, require('./routes/alterar-password.js'));
app.use('/eliminar-conta', checkLogged, require('./routes/eliminar-conta.js'));
app.use('/comprar-bilhete', checkLogged, require('./routes/comprar-bilhete.js'));
app.use('/logout', checkLogged, require('./routes/logout.js'));
app.use('/', checkLogged, require('./routes/root.js'));

app.use(function (req, res, next) {
    res.status(404).send('Página não encontrada!');
});

let port = process.env.WEBSITE_PORT || 3100;
app.listen(port, () => {
    console.log('-> Running on http://localhost:' + port);
});