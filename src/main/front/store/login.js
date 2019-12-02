import Vue from 'vue'
import Cookie from 'js-cookie';

export const state = () => ({
    authUser: { authenticated: false },
    session: null,
});

export const mutations = {
    setUser (
        state,
        authUser = null
    ) {
        let values = { authenticated: false }
        if (authUser !== null) {
            values = Object.assign(values, authUser)
        }
        for (const [
            key,
            value
        ] of Object.entries(values)) {
            Vue.set(state.authUser, key, value)
        }
    },
};

export const getters = {
    authenticated(state) {
        console.log('getter start');
        const hasAuthenticated = Reflect.has(state.authUser, 'authenticated');

        let authenticated = false
        console.log('hasAuthenticated', hasAuthenticated);
        if (hasAuthenticated) {
            authenticated = state.authUser.authenticated
        }
        return authenticated
    },
    authUser (state) {
        return state.authUser
    },
    isAdmin(state) {
        return state.authUser.mbrId === 'admin';
    },
};

export const actions = {
    async setUser({commit}, {id, password}) {
        try {
            const response = await this.$axios.$post(process.env.contextPath + '/login', '', {
                params: {
                    "grant_type": "password",
                    "client_id": "testClient",
                    "username": id,
                    "password": password,
                }
            });

            const user = Object.assign({}, response);
            user.authenticated = true;

            commit('setUser', user);
        }catch (e) {
            console.log("setUser", e);
            throw e;
        }
    },

    async getUser({commit}) {
        try {
            const response = await this.$axios.$post(process.env.contextPath + '/mbr/whois');

            const user = Object.assign({}, response.data);
            user.authenticated = true;

            commit('setUser', user);
        }catch (e) {
            console.log("getUser", e);
            throw e;
        }
    },

    async logout({commit, state}) {
        try {
            await this.$axios.$post(process.env.contextPath + '/logout')
            commit('setUser', null);
            //commit('menu/addMenu', null);
            Cookie.remove('auth');
            //Cookie.remove('menuList');
        }catch (e) {
            console.log("logout", e);
            throw e;
        }
    }
};