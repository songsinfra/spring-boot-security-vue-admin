<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_mbrInfo"
            @ok.prevent="submit"
            ref="modal"
            @show="showModal"
            centered
            :title="title">
        <b-form>
            <b-form-group
                    id="roleCd-group-1"
                    label="권한:"
                    label-for="roleCd-1"
            >
                <b-form-select
                        v-model="mbrInfo.roleCd"
                        :options="authList"
                        :value-field="'roleCode'"
                        :text-field="'codeNm'"
                        :disabled="state !=='CREATE' && !isAdmin"
                        :class="'form-control'"
                        v-validate="'required'"
                        data-vv-name="권한"
                >
                    <template v-slot:first>
                        <option
                                :value="undefined"
                                disabled
                        >-- 선택하세요 --</option>
                    </template>
                </b-form-select>
                <b-form-invalid-feedback :state="!errors.has('권한')">
                    {{errors.first('권한')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="mbrId-group-1"
                    label="아이디:"
                    label-for="mbrId-1"
                    :class="'mt-1'"
            >
                <b-form-input
                        id="mbrId-1"
                        v-model="mbrInfo.mbrId"
                        v-validate="'required|max:10'"
                        :disabled="state !=='CREATE'"
                        data-vv-name="아이디"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('아이디')">
                    {{errors.first('아이디')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="mbrPw-group-1"
                    label="비밀번호: (영문대소문자, 숫자, 특수문자 혼용해서 8~32자 이내)"
                    label-for="mbrPw-1"
            >
                <b-form-input
                        id="mbrPw-1"
                        v-model="mbrInfo.mbrPw"
                        type="password"
                        v-validate="{required : true, max:512 ,regex:/^.*(?=.{8,32})(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{6,}$/}"
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
            <b-form-group
                    id="mbrNm-group-1"
                    label="이름:"
                    label-for="mbrNm-1"
            >
                <b-form-input
                        id="mbrNm-1"
                        v-model="mbrInfo.mbrNm"
                        v-validate="'required|max:40'"
                        data-vv-name="이름"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('이름')">
                    {{errors.first('이름')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="input-group-1"
                    label="회사:"
                    label-for="mbrCompany-1"
            >
                <b-form-select
                        v-model="mbrInfo.mbrCompany"
                        :options="companyList"
                        :value-field="'mbrCompany'"
                        :text-field="'mbrCompany'"
                        :class="'form-control'"
                        @change="showMbrCompanyText = mbrInfo.mbrCompany === 'new'"
                >
                    <option :value="'new'">-- 직접입력 --</option>
                </b-form-select>
                <b-form-input
                        id="mbrCompanyText-1"
                        v-model="mbrInfo.mbrCompanyText"
                        v-if="showMbrCompanyText"
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="mbrDptmt-group-1"
                    label="부서:"
                    label-for="mbrDptmt-1"
            >
                <b-form-input
                        id="mbrDptmt-1"
                        v-model="mbrInfo.mbrDptmt"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="tel-group-1"
                    label="휴대폰:"
                    label-for="tel-1"
            >
                <b-form-input
                        id="tel-1"
                        v-model="mbrInfo.tel"
                        v-validate="'required|numeric'"
                        data-vv-name="휴대폰"
                        type="text"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('휴대폰')">
                    {{errors.first('휴대폰')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="email-group-1"
                    label="이메일:"
                    label-for="email-1"
            >
                <b-form-input
                        id="email-1"
                        v-model="mbrInfo.email"
                        v-validate="'required|email'"
                        data-vv-name="이메일"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('이메일')">
                    {{errors.first('이메일')}}
                </b-form-invalid-feedback>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        name: "ModalMbrInfo",

        props: ['selectedMbrInfo', 'state'],

        data() {
            return {
                mbrInfo: {},
                authList: [],
                companyList: [],
                showMbrCompanyText : false,
                title: '',
                errorBag:{
                    role: '권한',
                    id: '아이디',
                    name: '이름',
                    tel: '휴대폰',
                    email: '이메일'
                }
            };
        },

        computed:{
            isAdmin() {
                return this.$store.getters['login/isAdmin'];
            }
        },

        async beforeMount() {
            this.getAuthList();
            this.getCompanyList();
        },

        methods:{
            showModal() {
                this.$nextTick(async () => {
                    try {
                        if (this.$props.state === 'CREATE') {
                            this.mbrInfo = {
                                roleCd: 'R-002'
                            };
                            this.title = "회원정보 등록"
                            return;
                        }

                        this.title = "회원정보 수정"
                        this.mbrInfo = {...this.$props.selectedMbrInfo};

                    } catch (e) {

                    }
                });
            },

            async submit() {
                this.errors.clear();

                if (this.$props.state === 'CREATE') {
                    if(!await this.$validator.validate()) return;
                } else {
                    let checkElements = Object.values(this.errorBag);

                    if (this.mbrInfo.mbrPw || this.mbrInfo.mbrPwRe) {
                        checkElements.push(...['비밀번호', '비밀번호확인']);
                    }

                    if(await this.isInValidform(checkElements)) return;
                }


                 await this.ok();
            },

            async isInValidform(forms) {
                const results = await Promise.all(forms.map(e=>this.$validator.validate(e)));
                if(!results.every(e => e)) return true;

                return false;
            },

            async ok() {
                try {

                    const url = this.$props.state === 'CREATE' ? "/mbr/insertMbr" : "/mbr/updateMbr";
                    const mbrInfo = {...this.mbrInfo};

                    if (mbrInfo.mbrCompany === 'new') {
                        mbrInfo.mbrCompany = mbrInfo.mbrCompanyText;
                    }

                    if(mbrInfo.mbrPw != mbrInfo.mbrPwRe){
                        await this.$bvModal.msgBoxOk("입력한 비빌번호와 비빌번호 확인이 다릅니다.");
                        return;
                    }

                    if(this.$props.state === 'CREATE' && await this.isDuplicateMember(mbrInfo.mbrId, mbrInfo.tel)){
                        await this.$bvModal.msgBoxOk("등록된 ID가 있습니다.");
                        return;
                    }

                    if(this.$props.state === 'UPDATE'){
                        if (mbrInfo.mbrPw && mbrInfo.mbrPw.length > 0) mbrInfo.newPw = mbrInfo.mbrPw
                    }

                    const response = await this.$axios.$post(process.env.contextPath + url, mbrInfo);

                    if(mbrInfo.mbrPw && !this.isAdmin){
                        await this.$bvModal.msgBoxOk("저장이 완료 되었습니다. 변경된 비밀번호로 다시 로그인 해 주세요");
                        this.logout();
                    } else {
                        await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                        this.$eventBus.$emit('refreshMbrList')
                        this.$refs.modal.hide();
                    }

                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }

            },

            async isDuplicateMember(mbrId) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/mbr/isDuplicateMember',{mbrId});
                    return data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

           async getAuthList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/auth/selectRoleMstList');
                    this.authList = data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async getCompanyList() {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/mbr/getCompanyList');
                    this.companyList = data.data;
                } catch (e) {
                    e = (e.response && e.response.data) || e;
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            changePassword() {
                if (this.$props.state === 'UPDATE' && !this.mbrInfo.mbrPw && !this.mbrInfo.mbrPwRe) {
                    this.errors.clear();
                }
            },

            logout() {
                this.$store.dispatch('login/logout');
                this.$router.push("/login/login");
            }

        }


    }
</script>

<style scoped>

</style>