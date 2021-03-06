import Cookie from 'js-cookie';

export const state = () => ({
    menuList:[],
    upMenuList : [],
    useYnList : [
        {value : 'Y', text : '사용'},
        {value : 'N', text : '미사용'},
    ]
});

export const mutations = {
    addMenu(state, menu) {
        state.menuList = menu;
    },
    addUpMenuList(state, upMenuList) {
        state.upMenuList = upMenuList;
    }
};

export const actions = {
    async addMenu({commit}) {
        const menuList = await this.$axios.$post(process.env.contextPath + '/menu/getLayoutMenuList');
        commit('addMenu', menuList);
    },
    async addUpMenuList({commit}) {
        const menuList = await this.$axios.$post(process.env.contextPath + '/menu/getUpMenuList');
        commit('addUpMenuList', menuList);
    }
};