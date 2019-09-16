import Cookie from 'js-cookie';

export const state = () => {
    menuList:[]
};

export const mutations = {
    addMenu(state, menu) {
        state.menuList = menu;
    }
};

export const actions = {
    async addMenu({commit}) {
        const menuList = await this.$axios.$post('/api/menu/getLayoutMenuList');
        commit('addMenu', menuList);
        Cookie.set('menuList', menuList);
    }
};