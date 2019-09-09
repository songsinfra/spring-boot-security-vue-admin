import Cookie from 'js-cookie';

export const state = () => ({
    user:{}
});

export const mutations = {
    setUser(state, payload) {
        state.user = payload;
    }
};

export const actions = {
    async setUser({commit}, {id, password}) {
        try {
            // this.$axios.setToken("dGVzdENsaWVudDpzZWNyZXQ=", "basic");
            // const token = await this.$axios.$post('/api/oauth/token', '', {
            //     params: {
            //         "grant_type": "password",
            //         "client_id": "testClient",
            //         "username": id,
            //         "password": password,
            //     }
            // });
            // this.$axios.setToken(null, "basic");

            const response = await this.$axios.$post('/api/login', '', {
                params: {
                    "grant_type": "password",
                    "client_id": "testClient",
                    "username": id,
                    "password": password,
                }
            });

            debugger;
            commit('setUser', response);
            Cookie.set('auth', response);
        }catch (e) {
            console.log(e);
        }
    },

    async logout({commit, state}) {
        try {
            commit('setUser', null);
            Cookie.remove('auth');
        }catch (e) {
            console.log(e);
        }
    }
};