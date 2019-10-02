<template>
    <div class="main-menu menu-fixed menu-light menu-accordion menu-shadow " data-scroll-to-active="true"
         data-img="/images/backgrounds/02.jpg">
        <div class="navbar-header">
            <ul class="nav navbar-nav flex-row">
                <li class="nav-item mr-auto"><a class="navbar-brand" href="index.html"><img class="brand-logo"
                                                                                            alt="Chameleon admin logo"
                                                                                            src="/images/logo/logo.png"/>
                    <h3 class="brand-text">Chameleon</h3></a></li>
                <li class="nav-item d-md-none"><a class="nav-link close-navbar"><i class="ft-x"></i></a></li>
            </ul>
        </div>
        <div class="main-menu-content">
<!--            <ul class="navigation navigation-main" id="main-menu-navigation" data-menu="menu-navigation">-->
<!--                <li class="nav-item" v-for="menu in menuList" :key="menu.menuId" v-on:click="showMenu" v-bind:class="{ open : vMenu }">-->
<!--                    <a href="#" v-b-toggle="menu.menuId">-->
<!--                        <i class="ft-home"></i>-->
<!--                        <span class="menu-title" data-i18n="">{{menu.menuName}}</span>-->
<!--                        <span class="badge badge badge-info badge-pill float-right mr-2">{{menu.subMenu && menu.subMenu.length}}</span>-->
<!--                    </a>-->
<!--                    <b-collapse :id="menu.menuId">-->
<!--                        <ul class="menu-content">-->
<!--                            <li class="" v-for="subMenu in menu.subMenu" :key="subMenu.menuId">-->
<!--                                <nuxt-link :to="subMenu.menuURL">{{subMenu.menuName}}</nuxt-link>-->
<!--                            </li>-->
<!--                        </ul>-->
<!--                    </b-collapse>-->
<!--                </li>-->
<!--            </ul>-->

            <ul class="navigation navigation-main" id="main-menu-navigation" data-menu="menu-navigation">
                <li class=" nav-item" v-for="menu in menuList" :key="menu.menuId" v-on:click="showMenu" v-bind:class="{ open : vMenu }">
                    <a href="#">
                        <i class="ft-home"></i>
                        <span class="menu-title" data-i18n="">{{menu.menuName}}</span>
                        <span class="badge badge badge-info badge-pill float-right mr-2">{{menu.subMenu && menu.subMenu.length}}</span>
                    </a>
                    <ul class="menu-content">
                        <li class="" v-for="subMenu in menu.subMenu" :key="subMenu.menuId">
                            <nuxt-link :to="subMenu.menuURL">{{subMenu.menuName}}</nuxt-link>
                        </li>
                    </ul>
                </li>
                <li class=" nav-item"><a href="#"><i class="ft-layers"></i><span class="menu-title" data-i18n="">Apps</span></a>
                    <ul class="menu-content">
                        <li><a class="menu-item" href="email-application.html">Email Application</a>
                        </li>
                        <li><a class="menu-item" href="chat-application.html">Chat Application</a>
                        </li>
                        <li><a class="menu-item" href="full-calender.html">Full Calendar</a>
                        </li>
                        <li><a class="menu-item" href="project-summary.html">Project Summary</a>
                        </li>
                        <li><a class="menu-item" href="invoice-template.html">Invoice</a>
                        </li>
                        <li><a class="menu-item" href="#">Timelines</a>
                            <ul class="menu-content">
                                <li><a class="menu-item" href="timeline-center.html">Timelines Center</a>
                                </li>
                                <li><a class="menu-item" href="timeline-horizontal.html">Timelines Horizontal</a>
                                </li>
                            </ul>
                        </li>
                        <li><a class="menu-item" href="user-profile.html">Users Profile</a>
                        </li>
                        <li><a class="menu-item" href="users-contacts.html">Contact List</a>
                        </li>
                    </ul>
                </li>
                <li class=" nav-item"><a href="#"><i
                        class="ft-book"></i><span class="menu-title" data-i18n="">Documentation</span></a>
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
                vMenu: false,
                vMenuHover: false,
                menuList : []
            }
        },

        computed: {
            // menuList(){
            //     return this.$store.state.menu.menuList;
            // }
        },

        async beforeCreate() {
            console.log('process.server :', process.server);
            if(process.server) return;

            try {
                const menuList = await this.$axios.$post('/api/menu/getLayoutMenuList');
                this.menuList = menuList;
            } catch (e) {
                console.log("window 2------> ", e);
                if (e.response.status === 401|| e.response.status === 504) {
                    console.log("window ------> ", window);
                    window.location = '/login/login';
                } else {
                    console.dir( e);
                    await this.$bvModal.msgBoxOk(e.message);
                }
            }
        },

        methods:{
            goMenu({redirect, store}, menuId) {

                return redirect(menuId);
            },

            showMenu() {
                this.vMenu = !this.vMenu;
            }
        },


    }
</script>

<style scoped>

</style>