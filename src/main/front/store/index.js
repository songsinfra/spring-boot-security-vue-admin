import cookieparser from "cookieparser";

export const state = () => ({
    csrf : ''
});

export const mutations = {
    setCsrf(state, csrf) {
        state.csrf = csrf;
    }
};

export const actions = {
    async nuxtServerInit({commit}, {req}) {

        try {
            let auth = null
            debugger;
            console.log(req.headers.cookie);
            if (req.headers.cookie) {
                const parsed = cookieparser.parse(req.headers.cookie);
                console.log(parsed);
                try {
                    // auth = JSON.parse(parsed.auth)
                    auth = parsed.auth;
                    console.log(auth);
                } catch (err) {
                    console.log(err);
                }
            }
            commit('login/setUser', auth)
        } catch (e) {
            console.log(e);
        }
    },
};