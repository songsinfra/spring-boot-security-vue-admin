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
            const response = await this.$axios.$post('/api/login', '', {
                params: {
                    "grant_type": "password",
                    "client_id": "testClient",
                    "username": id,
                    "password": password,
                }
            });

            commit('setUser', response);
            Cookie.set('auth', response);
        }catch (e) {
            console.dir(e);
            throw e;
        }
    },

    async logout({commit, state}) {
        try {
            await this.$axios.$post('/api/logout')
            commit('setUser', null);
            //commit('menu/addMenu', null);
            Cookie.remove('auth');
            //Cookie.remove('menuList');
        }catch (e) {
            console.log(e);
        }
    }
};