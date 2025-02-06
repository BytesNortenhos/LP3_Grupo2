const axios = require('axios');
const { name } = require('ejs');

async function login(email, password) {
    return await axios({
        method: 'post',
        url: 'https://services.inapa.com/opo/api/client/login',
        data: {
            email: email,
            password: password
        },
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 200:
                return { stateLogin: 1, data: response.data, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(
                    error.response.data === 'InvalidIdentification' 
                    || error.response.data === 'InvalidParameters'
                ) return { stateLogin: 99, data: null, errorMsg: 'Email/Password errados!' };
                return { stateLogin: 99, data: null, errorMsg: 'Erro: ' + error.response.data };
                break;
            case 500:
                return { stateLogin: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data };
                break;
            default:
                return { stateLogin: 99, data: null, errorMsg: 'Erro Interno! Detalhes: ' + error.response.data };
        }
    });
}

async function registar(username, email, password) {
    return await axios({
        method: 'post',
        url: 'https://services.inapa.com/opo/api/client',
        data: {
            name: username,
            email: email,
            password: password
        },
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 201:
                return { stateLogin: 1, data: response.data, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(error.response.data === 'InvalidParameters') return { stateLogin: 99, data: null, errorMsg: 'Nome/Email/Password errados!' };
                if(error.response.data.Message === 'Email already registered') return { stateLogin: 99, data: null, errorMsg: 'O email inserido já foi usado!' };
                return { stateLogin: 99, data: null, errorMsg: 'Erro: ' + error.response.data.Message };
                break;
            case 500:
                return { stateLogin: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data };
                break;
            default:
                return { stateLogin: 99, data: null, errorMsg: 'Erro Interno! Detalhes: ' + error.response.data };
        }
    });
}

async function alterarPassword(userId, newPassword) {
    return await axios({
        method: 'put',
        url: 'https://services.inapa.com/opo/api/client/' + userId,
        data: {
            password: newPassword,
        },
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 202:
                return { stateLogin: 1, data: null, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(error.response.data === 'InvalidParameters') return { stateLogin: 99, data: null, errorMsg: 'UserId/Password inexistente!' };
                return { stateLogin: 99, data: null, errorMsg: 'Erro: ' + error.response.data };
                break;
            case 500:
                return { stateLogin: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data };
                break;
            default:
                return { stateLogin: 99, data: null, errorMsg: 'Erro Interno! Detalhes: ' + error.response.data };
        }
    });
}

async function eliminarConta(userId) {
    return await axios({
        method: 'delete',
        url: 'https://services.inapa.com/opo/api/client/' + userId,
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 202:
                return { stateRequest: 1, data: null, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(error.response.data === 'InvalidParameters') return { stateRequest: 99, data: null, errorMsg: 'UserId inexistente!' };
                if(error.response.data.Message === 'Client cannot be deleted') return { stateRequest: 99, data: null, errorMsg: 'Não pode eliminar esta conta!' };
                return { stateRequest: 99, data: null, errorMsg: 'Erro: ' + error.response.data };
                break;
            case 500:
                return { stateRequest: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data.Message };
                break;
            default:
                return { stateRequest: 99, data: null, errorMsg: 'Erro Interno! Detalhes: ' + error.response.data.Message };
        }
    });
}

async function obterTickets(userId) {
    return await axios({
        method: 'get',
        url: 'https://services.inapa.com/opo/api/ticket/client/' + userId,
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 200:
                return { stateRequest: 1, data: response.data.TicketInfo, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(error.response.data === 'InvalidIdentification') return { stateRequest: 1, data: [], errorMsg: null };
                if(error.response.data === 'InvalidParameters') return { stateRequest: 99, data: null, errorMsg: 'Erro ao obter os tickets!' };
                return { stateRequest: 99, data: null, errorMsg: 'Erro ao obter os tickets! Detalhes: ' + error.response.data };
                break;
            case 500:
                return { stateRequest: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data };
                break;
            default:
                return { stateRequest: 99, data: null, errorMsg: 'Erro! Detalhes: ' + error.response.data };
        }
    });
}

async function obterGames() {
    return await axios({
        method: 'get',
        url: 'https://services.inapa.com/opo/api/game/',
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 200:
                return { stateRequest: 1, data: response.data.Games, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(error.response.data === 'InvalidIdentification') return { stateRequest: 1, data: [], errorMsg: null };
                if(error.response.data === 'InvalidParameters') return { stateRequest: 99, data: null, errorMsg: 'Erro ao obter os jogos!' };
                return { stateRequest: 99, data: null, errorMsg: 'Erro ao obter os jogos! Detalhes: ' + error.response.data };
                break;
            case 500:
                return { stateRequest: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data };
                break;
            default:
                return { stateRequest: 99, data: null, errorMsg: 'Erro! Detalhes: ' + error.response.data };
        }
    });
}

async function comprarBilhete(idGame, idClient) {
    let numTickets = await obterNumTickets(idGame);
    console.log(numTickets);
    if(numTickets === -1) return { stateRequest: 99, data: null, errorMsg: 'Erro ao gerar o lugar!' };

    return await axios({
        method: 'post',
        url: 'https://services.inapa.com/opo/api/ticket',
        data: {
            clientId: idClient,
            gameId: idGame,
            seat: numTickets + 1,
        },
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 201:
                return { stateRequest: 1, data: response.data, errorMsg: null };
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                //-> Pode ser: falta de parametros, seat já ocupado, utilizador ou game inexistente, bilhete já comprado para o utilizador
                if(error.response.data.Message === 'Client Id or Product Id does not exist') return { stateRequest: 99, data: null, errorMsg: 'Erro ao comprar o bilhete!' };
                return { stateRequest: 99, data: null, errorMsg: 'Erro: ' + error.response.data.Message };
                break;
            case 500:
                return { stateRequest: 99, data: null, errorMsg: 'Erro no Servidor! Detalhes: ' + error.response.data };
                break;
            default:
                return { stateRequest: 99, data: null, errorMsg: 'Erro Interno! Detalhes: ' + error.response.data };
        }
    });
}

async function obterNumTickets(idGame) {
    return await axios({
        method: 'get',
        url: 'https://services.inapa.com/opo/api/ticket/game/' + idGame,
        headers: {
            'Content-Type': 'application/json',
        },
        auth: {
            username: process.env.OPO_API_USERNAME,
            password: process.env.OPO_API_PASSWORD
        }
    }).then((response) => {
        switch(response.status) {
            case 200:
                return response.data.TicketInfo.length;
                break;
        }
    }).catch((error) => {
        switch(error.status) {
            case 406:
                if(error.response.data === 'InvalidIdentification') return 0;
                if(error.response.data === 'InvalidParameters') return -1;
                return -1;
                break;
            case 500:
                return -1;
                break;
            default:
                return -1;
        }
    });
}

module.exports = {
    login,
    registar,
    alterarPassword,
    eliminarConta,
    obterTickets,
    obterGames,
    comprarBilhete
};