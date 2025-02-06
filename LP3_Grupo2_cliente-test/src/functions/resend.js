const { Resend } = require('resend');
const resend = new Resend(process.env.RESEND_API_KEY);

async function enviarEmail(username, email, password) {

    let { data, error } = await resend.emails.send({
        from: `Olimpíadas dos Mancos <no-reply-pls@robertovalente.pt>`,
        to: email,
        subject: "Criação de Conta",
        html: `<h1>Olá ${username},</h1> <p>A sua conta foi criada com sucesso!</p> <p>Por favor faça login com os seguintes dados:</p> <p>- Email: ${email}</p> <p>- Password: ${password}</p>`,
    });

    if(error) return { status: 0, message: error.message };
    return { status: 1, message: data };
}

module.exports = {
    enviarEmail,
}