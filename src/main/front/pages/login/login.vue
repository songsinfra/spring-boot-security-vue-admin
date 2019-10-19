<template>
    <div class="app-content content">
        <div class="content-wrapper">
            <div class="content-wrapper-before"></div>
            <div class="content-header row">
            </div>
            <div class="content-body">
                <section class="flexbox-container">
                    <div class="col-12 d-flex align-items-center justify-content-center">
                        <div class="col-md-4 col-10 box-shadow-2 p-0">
                            <div class="card border-grey border-lighten-3 px-1 py-1 m-0">
                                <div class="card-header border-0">
                                    <div class="text-center mb-1">
                                        <img src="/images/logo/logo.png" alt="branding logo">
                                    </div>
                                    <div class="font-large-1  text-center">
                                        Member Login
                                    </div>
                                </div>
                                <div class="card-content">

                                    <div class="card-body">
                                        <form class="form-horizontal" action="index.html" novalidate @submit.prevent="login">
                                            <fieldset class="form-group position-relative has-icon-left">
                                                <input type="text" class="form-control round" id="user-name"
                                                       placeholder="Your Username" v-model="user.username" required >
                                                <div class="form-control-position">
                                                    <i class="ft-user"></i>
                                                </div>
                                            </fieldset>
                                            <fieldset class="form-group position-relative has-icon-left">
                                                <input type="password" class="form-control round" id="user-password"
                                                       placeholder="Enter Password" required v-model="user.password" >
                                                <div class="form-control-position">
                                                    <i class="ft-lock"></i>
                                                </div>
                                            </fieldset>

                                            <div class="form-group text-center">
                                                <button type="submit"
                                                        class="btn round btn-block btn-glow btn-bg-gradient-x-purple-blue col-12 mr-1 mb-1">
                                                    Login
                                                </button>
                                            </div>

                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</template>


<script>
    export default {
        layout: 'login',
        name: 'Login',
        data: ()=>{
            return {
                user: {
                    username: '',
                    password: '',
                }
            }
        },
        methods : {
            async login(){
                try {
                    await this.$store.dispatch('login/setUser', {id: this.user.username, password: this.user.password});
                    // this.$router.push("/");
                    window.location = '/';
                } catch (e) {
                    const msg = e.response.data.message || e.response.data;
                    await this.$bvModal.msgBoxOk(msg);
                }
            }
        }
    }
</script>
<style scoped>

</style>