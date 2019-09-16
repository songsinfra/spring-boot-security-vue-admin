import cookieparser from "cookieparser";

export const state = () => ({
    csrf : '',
    error : {}
});

export const mutations = {
    setCsrf(state, csrf) {
        state.csrf = csrf;
    },
    setError(state, error) {
        state.error = error;
    }
};

export const actions = {
    async nuxtServerInit({commit, dispatch, state}, {req}) {
        try {
            setUserInfo(state, req, commit);
            setMenuList(state, req, commit);
        } catch (e) {
            console.log(e);
        }
    },
};

function setMenuList(state, req, commit) {
    if (!state.menu.menuList) {
        const parsed = cookieparser.parse(req.headers.cookie);
        try {

            commit('menu/addMenu', JSON.parse(parsed.menuList));
        } catch (err) {
            console.log(err);
        }
    }
}

function setUserInfo(state, req, commit) {
    let auth = null

    if(!state.login.user) return;

    console.log(req.headers.cookie);
    if (req.headers.cookie) {
        const parsed = cookieparser.parse(req.headers.cookie);
        try {
            auth = parsed.auth;
        } catch (err) {
            console.log(err);
        }
    }
    commit('login/setUser', JSON.parse(auth));
}