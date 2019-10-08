<template>
    <div>
        <base-top/>
        <base-left-menu/>
        <div class="app-content content">
            <div class="content-wrapper">
                <div class="content-wrapper-before"></div>
                <div class="content-header row">
                    <div class="content-header-left col-md-6 col-12 mb-0">
                        <h3 class="content-header-title">{{menuName}}</h3>
                    </div>
                    <div class="content-header-right col-md-6 col-12">
                        <div class="breadcrumbs-top float-md-right">
                            <div class="breadcrumb-wrapper mr-1">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="#">Home</a>
                                    </li>
                                    <li class="breadcrumb-item"><a href="#">{{parantMenuName}}</a>
                                    </li>
                                    <li class="breadcrumb-item active">{{menuName}}</li>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="content-body">
                    <nuxt/>
                </div>
            </div>
        </div>
        <base-footer/>
    </div>
</template>

<script>
    import BaseLeftMenu from '~/components/layout/BaseLeftMenu.vue';
    import BaseTop from '~/components/layout/BaseTop.vue';
    import BaseFooter from '~/components/layout/BaseFooter.vue';

    export default {
        name: "default",

        components: {
            BaseTop,
            BaseLeftMenu,
            BaseFooter
        },

        fetch({store}) {
        },

        computed : {
            menuName(){
                return this.$store.state.currentMenu.menuName;
            },

            parantMenuName(){
                return this.$store.state.currentMenu.parantMenuName;
            }
        },

        head() {
            return {
                // link: [
                //     {rel: 'apple-touch-icon', href: '/images/ico/apple-icon-120.png'},
                //     {rel: 'shortcut icon', href: '/images/ico/favicon.ico'},
                //     {
                //         rel: 'stylesheet',
                //         href: 'https://fonts.googleapis.com/css?family=Muli:300,300i,400,400i,600,600i,700,700i%7CComfortaa:300,400,700'
                //     },
                //     {
                //         rel: 'stylesheet',
                //         href: 'https://maxcdn.icons8.com/fonts/line-awesome/1.1/css/line-awesome.min.css'
                //     },
                //     {rel: 'stylesheet', href: '/vendors/css/charts/chartist.css'},
                //     {rel: 'stylesheet', href: '/vendors/css/charts/chartist-plugin-tooltip.css'},
                //     {rel: 'stylesheet', href: '/css/vendors.css'},
                //     {rel: 'stylesheet', href: '/css/app.css'},
                //     {rel: 'stylesheet', href: '/css/core/menu/menu-types/vertical-menu.css'},
                //     {rel: 'stylesheet', href: '/css/core/colors/palette-gradient.css'},
                //     {rel: 'stylesheet', href: '/css/pages/chat-application.css'},
                //     {rel: 'stylesheet', href: '/css/pages/dashboard-analytics.css'},
                // ],
                // meta: {
                //     viewport: 'width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui'
                // },
                bodyAttrs: {
                    class: 'vertical-layout vertical-menu 2-columns menu-expanded fixed-navbar',
                    ['data-col']: '2-column',
                    ['data-color']  : 'bg-gradient-x-purple-blue',
                    ['data-menu'] : 'vertical-menu'
                },
                script: [
                    { src:'/vendors/js/vendors.min.js'},
                    { src:'/js/core/app-menu.js'},
                    { src:'/js/core/app.js'},
                ],
            }
        },

    }
</script>

<style scoped>
</style>