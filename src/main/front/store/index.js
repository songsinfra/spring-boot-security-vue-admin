export const state = () => ({
    currentMenu : {},
});

export const mutations = {
    SET_CURRENT_MENU(state, menu) {
        state.currentMenu = menu;
    },
};

export const actions = {
    async nuxtServerInit({commit, dispatch, state}, {req}) {
        try {
        } catch (e) {
            console.log(e);
        }
    },
};

