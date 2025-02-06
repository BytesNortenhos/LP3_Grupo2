function checkLogged (req, res, next) {
    if(req.isAuthenticated()) {
        next();
    } else {
        res.redirect('/login');
    }
}

function checkNotLogged (req, res, next) {
    if(!req.isAuthenticated()) return next();

    return res.redirect('/');
}

module.exports = {
    checkLogged,
    checkNotLogged
}