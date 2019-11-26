<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_update_pw"
            ref="modal"
            centered
            @ok.prevent="submit"
            @show="showModal"
            @hidden="hide"
            :title="title">
        <b-form>
            <b-form-group
                    id="oriMbrPw-group-1"
                    label="현재 비밀번호:"
                    label-for="mbrPw-1"
            >
                <b-form-input
                        id="oriMbrPw-1"
                        v-model="mbrInfo.oriMbrPw"
                        type="password"
                        v-validate="'required'"
                        data-vv-name="현재비밀번호"
                        @change="changePassword"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('현재비밀번호')">
                    {{errors.first('현재비밀번호')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="mbrPw-group-1"
                    label="새로운 비밀번호: (영문대소문자, 숫자, 특수문자 혼용해서 8~32자 이내) "
                    label-for="mbrPw-1"
            >
                <b-form-input
                        id="mbrPw-1"
                        v-model="mbrInfo.mbrPw"
                        type="password"
                        v-validate="{required : true ,regex:/^.*(?=.{8,32})(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{6,}$/}"
                        data-vv-name="비밀번호"
                        ref="password"
                        @change="changePassword"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('비밀번호')">
                    {{errors.first('비밀번호')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="mbrPwRe-group-1"
                    label="비밀번호 확인:"
                    label-for="mbrPwRe-1"
            >
                <b-form-input
                        id="mbrPwRe-1"
                        v-model="mbrInfo.mbrPwRe"
                        type="password"
                        v-validate="'required|confirmed:password'"
                        data-vv-name="비밀번호확인"
                        @change="changePassword"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('비밀번호확인')">
                    {{errors.first('비밀번호확인')}}
                </b-form-invalid-feedback>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        name: "ModalUpdatePw",

        props: ['mbrId'],

        data() {
            return {
                mbrInfo: {},
                title: '',
            };
        },

        methods:{
            showModal() {
            },

            async submit() {
                this.errors.clear();

                if(!await this.$validator.validate()) return;

                await this.ok();
            },

            async ok() {
                try {
                    const mbrInfo = {
                        mbrId: this.mbrId,
                        oriMbrPw: this.mbrInfo.oriMbrPw,
                        mbrNewPw: this.mbrInfo.mbrPw
                    };

                    await this.validationForUpdate(this.mbrId, mbrInfo.oriMbrPw, mbrInfo.mbrNewPw);

                    const response = await this.$axios.$post(process.env.contextPath + '/mbr/updatePwd', mbrInfo);

                    await this.$bvModal.msgBoxOk("변경이 완료 되었습니다.");
                    window.location = '/';

                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async isExistLoginInfo(mbrId, mbrPw) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/mbr/isExistLoginInfo', {mbrId, mbrPw});
                    debugger;
                    return data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async validationForUpdate(mbrId, oriMmbrPw, mbrPw) {
                const isExist = await this.isExistLoginInfo(mbrId, oriMmbrPw);
                if(!isExist) throw new Error("현재 비밀번호가 틀립니다");
            },

            changePassword() {
                if (this.$props.state === 'UPDATE' && !this.mbrInfo.mbrPw && !this.mbrInfo.mbrPwRe) {
                    this.errors.clear();
                }
            },

            hide() {
                window.location = '/';
            }
        }



    }
</script>

<style scoped>

</style>