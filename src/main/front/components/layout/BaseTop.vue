<template>
    <nav class="header-navbar navbar-expand-md navbar navbar-with-menu navbar-without-dd-arrow fixed-top navbar-semi-light">
        <div class="navbar-wrapper">
            <div class="navbar-container content">
                <div class="collapse navbar-collapse show" id="navbar-mobile">
                    <ul class="nav navbar-nav mr-auto float-left">
                        <li class="nav-item mobile-menu d-md-none mr-auto"><a
                                class="nav-link nav-menu-main menu-toggle hidden-xs" href="#"><i
                                class="ft-menu font-large-1"></i></a></li>
                        <li class="nav-item d-none d-md-block"><a class="nav-link nav-menu-main menu-toggle hidden-xs"
                                                                  href="#"><i class="ft-menu"></i></a></li>
                    </ul>
                    <ul class="nav navbar-nav float-right">
                        <li class="dropdown dropdown-user nav-item" :class="{show : isShowProfile}"><a class="dropdown-toggle nav-link dropdown-user-link"
                                                                       href="#" @click="isShowProfile = !isShowProfile"> <span
                                class="avatar avatar-online"><img src="/images/portrait/small/avatar-s-19.png"
                                                                  alt="avatar"></span></a>
                            <div class="dropdown-menu dropdown-menu-right">
                                <div class="arrow_box_right"><a class="dropdown-item" href="#"><span
                                        class="avatar avatar-online"><img
                                        src="/images/portrait/small/avatar-s-19.png" alt="avatar"><span
                                        class="user-name text-bold-700 ml-1">John Doe</span></span></a>
                                    <div class="dropdown-divider"></div>
                                    <a class="dropdown-item" @click="logout"><i class="ft-power"></i> Logout</a>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </nav>
</template>

<script>
    export default {
        name: "Top",
        methods:{
            logout() {
                this.$store.dispatch('login/logout');
                this.$router.push("/login/login");
            }
        },
        data() {
            return {
                isShowProfile: false
            }
        }

    }
</script>

<style scoped>

</style>