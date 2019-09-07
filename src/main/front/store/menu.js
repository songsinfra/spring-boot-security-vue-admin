export const state = () => {
    menus:[]
};

export const mutations = {
    addMenu(state, menu) {
        state.menus.push(menu);
    }
};

export const actions = {
    addMenu({commit}, menu) {

        this.$axios.$post('')

        commit('menu/addMenu', menu);
    }
};