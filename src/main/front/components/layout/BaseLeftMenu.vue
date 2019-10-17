<template>
    <div class="main-menu menu-fixed menu-light menu-accordion menu-shadow " data-scroll-to-active="true"
         data-img="/images/backgrounds/02.jpg">
        <div class="navbar-header">
            <ul class="nav navbar-nav flex-row">
                <li class="nav-item mr-auto"><a class="navbar-brand" href="index.html"><img class="brand-logo"
                                                                                            alt="Chameleon admin logo"
                                                                                            src="/images/logo/logo.png"/>
                    <h3 class="brand-text">GFN-admin</h3></a></li>
                <li class="nav-item d-md-none"><a class="nav-link close-navbar"><i class="ft-x"></i></a></li>
            </ul>
        </div>
        <div class="main-menu-content">
            <ul class="navigation navigation-main" id="main-menu-navigation" data-menu="menu-navigation">
                <li class=" nav-item has-sub" v-for="(menu, index) in menuList" :key="menu.menuId" >
                    <a href="#" >
                        <i :class="menuIcons[index]"></i>
                        <span class="menu-title" data-i18n="">{{menu.menuName}}</span>
                        <span class="badge badge badge-info badge-pill float-right mr-2">{{menu.subMenu && menu.subMenu.length}}</span>
                    </a>
                    <ul class="menu-content">
                        <li class="" v-for="subMenu in menu.subMenu" :key="subMenu.menuId">
                            <a @click.prevent="goMenu(subMenu)">{{subMenu.menuName}}</a>
<!--                            <nuxt-link :to="subMenu.menuURL">{{subMenu.menuName}}</nuxt-link>-->
                        </li>
                    </ul>
                </li>

            </ul>
        </div>
        <div class="navigation-background"></div>
    </div>
</template>

<script>
    export default {
        name: "LeftMenu",
        data: () => {
            return {
                vMenu: '',
                vMenuHover: false,
                menuIcons: [],
                // menuList : []
            }
        },

        computed:{
            menuList() {
                return this.$store.state.menu.menuList;
            }
        },

        created() {
            this.menuIcons = [
                'ft-gitlab',
                'ft-cpu',
                'ft-cloud-snow',
                'ft-command',
                'ft-codepen',
                'ft-box',
                'ft-crosshair',
                'ft-grid',
                'ft-aperture',
                'ft-aperture'
            ];
        },

        async beforeCreate() {
            console.log('process.server :', process.server);
            if(process.server) return;

            console.log('this.$router', this.$router.path);

            try {
                const menuList = await this.$axios.$post(process.env.contextPath + '/menu/getLayoutMenuList');
                this.$store.commit('menu/addMenu', menuList);

                if(!this.$store.state.menu.currentMenu) {
                    const urlPath = this.$router.currentRoute.path;
                    if(urlPath !== '/')
                        this.$store.commit('SET_CURRENT_MENU', this.initCurrentMenu(menuList, urlPath));
                }

            } catch (e) {
                console.error(e);
                if (e.response.status === 401|| e.response.status === 504) {
                    this.$router.push('login/login');
                    // window.location = '/login/login';
                } else {
                    console.dir( e);
                    await this.$bvModal.msgBoxOk(e.message);
                }
            }
        },

        methods:{
            goMenu(menu) {
                console.log(this);
                this.$store.commit("SET_CURRENT_MENU", menu);
                this.$router.push(menu.menuURL);
            },

            initCurrentMenu(menuList, urlPath) {
                const subMenu = menuList.map(menu => menu.subMenu).flat()
                                .find(subMenu=>subMenu.menuURL === urlPath);

                return subMenu;
            }

        },


    }
</script>

<style scoped>

</style>