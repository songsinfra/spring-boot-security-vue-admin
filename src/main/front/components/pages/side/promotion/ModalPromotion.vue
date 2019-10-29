<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_promotion"
            centered
            @ok.prevent="submit"
            ref="modal"
            @show="showModal"
            :title="title">
        <template v-slot:modal-ok="{item}">
            {{okButtonTitle}}
        </template>
        <b-form>
            <b-form-group
                    label="코드:"
                    label-for="promoCode-1"
                    v-if="state === 'UPDATE'"
            >
                <b-form-input
                        id="promoCode-1"
                        v-model="promoInfo.promoCode"
                        :disabled="state === 'UPDATE'"
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    label="이메일:"
                    label-for="email-1"
            >
                <b-form-input
                        id="email-1"
                        v-model="promoInfo.email"
                        v-validate="'required|email'"
                        data-vv-name="이메일"
                        :disabled="state === 'UPDATE'"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('이메일')">
                    {{errors.first('이메일')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    label="연락처:"
                    label-for="contactNo-1"
            >
                <b-form-input
                        id="contactNo-1"
                        v-model="promoInfo.contactNo"
                        :disabled="state === 'UPDATE'"
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="useYn-group-1"
                    label="상태:"
                    label-for="useYn-1"
                    v-if="state === 'UPDATE'"
            >
                <b-form-select
                        id="statusCd-1"
                        v-model="promoInfo.statusCd"
                        v-validate="'required'"
                        data-vv-name="상태"
                        :disabled="state === 'UPDATE'"
                >
                    <option value="0">미사용</option>
                    <option value="1">사용</option>
                    <option value="2">만료</option>
                </b-form-select>
                <b-form-invalid-feedback :state="!errors.has('상태')">
                    {{errors.first('상태')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    label="유효기간:"
                    label-for="dueDt-1"
            >
                <b-form-input
                        id="dueDt-1"
                        v-model="dueDt"
                        v-validate="'required'"
                        type="date"
                        data-vv-name="유효기간"
                ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('유효기간')">
                    {{errors.first('유효기간')}}
                </b-form-invalid-feedback>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        props: ['selectedPromo', 'state'],
        name: "ModalPromotion",

        data() {
            return {
                promoInfo: {},
                title : '',
                dueDt: '',
                okButtonTitle:''
            };
        },
        computed: {

        },

        methods:{
            async submit() {
                if(!await this.$validator.validate()) {
                    console.dir(this.$validator);
                    return;
                }

                if(this.$props.state === 'CREATE' && await this.isExistPromo() &&
                    !await this.$bvModal.msgBoxConfirm("동일한 요청정보로 생성된 프로모션 코드가 있습니다. \n추가발급 하시겠습니까.")) return;

                if(await this.isInvalidDueDt()) return;

                await this.ok();
            },

            async ok() {
                try {
                    const {url, params} = this.$props.state === 'CREATE' ? this.create() : this.update();
                    const {data} = await this.$axios.$post( process.env.contextPath + url, params);

                    if(this.$props.state === 'CREATE'){
                        await this.$bvModal.msgBoxOk(this.createSuccessMsg(data, this.dueDt));
                    }else{
                        await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    }

                    this.$eventBus.$emit('refreshPromoList');
                    this.$refs.modal.hide();
                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            createSuccessMsg(code, dueDt) {
                const promoCode = code.match(/.{1,4}/g).join('-');

                const h = this.$createElement;
                return h('b-card', [
                    h('b-card-text', { class: [] }, [
                        h('h2', [
                            '프로모션 코드',
                        ]),
                    ]),
                    h('b-card',{
                        props:{
                            'border-variant' : "primary",
                        }
                    },[
                        h('h2', { class: ['text-danger'], props:{
                                'vertical-align':"center"
                            } }, [
                            promoCode,
                        ]),
                        h('b-card-text', { class: [] }, [
                            '만료일 : ' +  dueDt,
                        ]),
                    ]),
                    h('b-card-text', { class: [] }, [
                        '위의 프로모션 코드를 요청자의 이메일로 직접 전달하기 바랍니다.',
                    ]),
                ]);
            },

            create() {
                return {
                    url: "/side/insertPromo",
                    params: {
                        email: this.promoInfo.email,
                        dueDt: this.dueDt && this.dueDt.replace(/-/g, ''),
                        contactNo: this.promoInfo.contactNo,
                    }
                };
            },

            update() {
                return {
                    url: "/side/updatePromo",
                    params: {
                        promoCode: this.promoInfo.promoCode,
                        dueDt: this.dueDt && this.dueDt.replace(/-/g, ''),
                        statusCd : this.promoInfo.statusCd,
                    }
                };
            },

            async showModal() {
                try {
                    this.errors.clear();
                     this.$nextTick(async () => {
                        if (this.$props.state === 'CREATE') {
                            this.promoInfo = {};
                            this.dueDt = this.$moment().add(30, 'days').format('YYYY-MM-DD')
                            this.title = '프로모션 요청 정보';
                            this.okButtonTitle = "OK";
                            return;
                        }

                        this.title = "프로모션 코드 변경";
                        this.promoInfo = {...this.$props.selectedPromo};
                        this.dueDt = this.$moment(this.promoInfo.dueDt).format('YYYY-MM-DD');
                        this.okButtonTitle = this.promoInfo.statusCd === '2' ? '재사용' : '기간연장';

                     });
                } catch (e) {
                    await this.$bvModal.msgBoxOk(e.message);
                }
            },

            async isInvalidDueDt() {
                const today = this.$moment();
                const changeDate = this.$moment(this.dueDt, 'YYYY-MM-DD');
                if(changeDate.isBefore(today.clone().subtract(1,"days"))) {
                    await this.$bvModal.msgBoxOk(`${today.format("YYYY-MM-DD")} 보다 이전 날짜로 설정이 불가능합니다.`);
                    return true;
                }

                return false;
            },

            async isExistPromo() {
                const {data} = await this.$axios.$post(process.env.contextPath + '/side/existPromo', {
                    email: this.promoInfo.email,
                    contactNo: this.promoInfo.contactNo
                });

                return data;
            },
        }
    }
</script>

<style scoped>

</style>