<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_authInfo"
            @ok.prevent="ok"
            ref="modal"
            @show="showModal"
            centered
            :title="title">
        <b-form>
            <b-form-group
                    id="roleCd-group-1"
                    label="권한코드:"
                    label-for="roleCd-1"
            >
                <b-form-input
                        id="roleCd-1"
                        v-model="authInfo.roleCode"
                        :disabled="state !=='CREATE'"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="codeNm-group-1"
                    label="권한명:"
                    label-for="codeNm-1"
                    :class="'mt-1'"
            >
                <b-form-input
                        id="codeNm-1"
                        v-model="authInfo.codeNm"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="useYn-group-1"
                    label="사용여부:"
                    label-for="useYn-1"
            >
                <b-form-select
                        id="useYn-1"
                        :required="true"
                        v-model="authInfo.useYn"
                        :options="useYnOption"
                ></b-form-select>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        name: "ModalAuthInfo",

        props: ['selectedAuthInfo', 'state'],

        computed: {
            useYnOption() {
                return this.$store.state.menu.useYnList;
            }
        },

        data() {
            return {
                authInfo: {},
                authList: [],
                companyList: [],
                showMbrCompanyText : false,
                title: ''
            };
        },

        methods:{
            showModal() {
                this.$nextTick(async () => {
                    try {
                        this.errors.clear();

                        if (this.$props.state === 'CREATE') {
                            this.authInfo = { useYn : 'Y'};
                            this.title = '권한정보 등록';
                            return;
                        }

                        this.title = '권한정보 수정';
                        this.authInfo = {...this.$props.selectedAuthInfo};
                    } catch (e) {

                    }
                });
            },

            async ok() {
                try {

                    const url = this.$props.state === 'CREATE' ? "/auth/insertRoleMst" : "/auth/updateRoleMst";
                    const authInfo = {...this.authInfo};

                    const response = await this.$axios.$post(process.env.contextPath + url, authInfo);

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    this.$eventBus.$emit('refreshAuthList')
                    this.$refs.modal.hide();
                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }

            },
        }


    }
</script>

<style scoped>

</style>