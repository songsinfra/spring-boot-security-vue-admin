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
                                        <form class="form-horizontal" novalidate @submit.prevent="login">
                                            <fieldset class="form-group position-relative has-icon-left">
                                                <input type="text"
                                                       class="form-control round"
                                                       id="user-name"
                                                       placeholder="Your Username"
                                                       v-model="user.username"
                                                       v-validate="'required'"
                                                       data-vv-name="아이디"
                                                >
                                                <b-form-invalid-feedback :state="!errors.has('아이디')">
                                                    {{errors.first('아이디')}}
                                                </b-form-invalid-feedback>
                                                <div class="form-control-position">
                                                    <i class="ft-user"></i>
                                                </div>
                                            </fieldset>
                                            <fieldset class="form-group position-relative has-icon-left">
                                                <input
                                                        type="password"
                                                        class="form-control round"
                                                        id="user-password"
                                                        placeholder="Enter Password"
                                                        v-model="user.password"
                                                        v-validate="'required'"
                                                        data-vv-name="비밀번호"
                                                >
                                                <b-form-invalid-feedback :state="!errors.has('비밀번호')">
                                                    {{errors.first('비밀번호')}}
                                                </b-form-invalid-feedback>
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
        <modal-update-pw :mbr-id="user.username"/>
    </div>
</template>


<script>
    import ModalUpdatePw from '~/components/pages/mbrInfoList/ModalUpdatePw.vue';

    export default {
        layout: 'login',
        name: 'Login',

        components: {ModalUpdatePw},

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
                    if(!await this.$validator.validate()) {
                        console.dir(this.$validator);
                        return;
                    }

                    await this.$store.dispatch('login/setUser', {id: this.user.username, password: this.user.password});

                    if(await this.isExpirePasswordDuration(this.user.username) &&
                        await this.$bvModal.msgBoxConfirm("개인정보 보호를 위해서 주기적(최소 3개월)으로 비밀번호를 변경해주세요. 변경하시겠습니까?")){
                        this.$bvModal.show('modal_update_pw');
                    } else {
                        window.location = '/';
                    }
                } catch (e) {
                    const msg = e.response.data.message || e.response.data;
                    await this.$bvModal.msgBoxOk(msg);
                }
            },

            async isExpirePasswordDuration(mbrId) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/login/isExpirePasswordDuration',{mbrId});
                    return data.data;
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

        }
    }
</script>
<style scoped>

</style>