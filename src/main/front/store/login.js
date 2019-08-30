import axios from '@nuxtjs/axios';

export const state = () => ({
    user:{}
});

export const mutations = {
    setUser(state, payload) {
        state.user = payload;
    }
};

export const actions = {
    async setUser({commit}) {
        try {
            debugger;
            const res = await axios.post('http://localhost:8080/oauth/token', {
                "grant_type" 	: "password",
                "client_id" 	: "testClient",
                "username" 	: "test",
                "password" 	: "test"
            });
            console.log(res.data);
            commit('setUser', res.data);
        }catch (e) {
            console.log(e);
        }

    }
};