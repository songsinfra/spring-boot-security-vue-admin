<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_addInfo"
            @ok.prevent="ok"
            ref="modal"
            @show="showModal"
            centered
            title="부가서비스정보 수정">
        <b-form>
            <b-form-group
                    id="addItemType-group-1"
                    label="상품구분:"
                    label-for="addItemType-1"
            >
                <b-form-radio v-model="addInfo.addItemType" name="some-radios" value="U">U-Cube 연동 상품</b-form-radio>
                <b-form-radio v-model="addInfo.addItemType" name="some-radios" value="G">NVIDIA 직업 연동 상품</b-form-radio>
            </b-form-group>
            <b-form-group
                    id="addItemNm-group-1"
                    label="부가서비스명:"
                    label-for="addItemNm-1"
                    :class="'mt-1'"
            >
                <b-form-input
                        id="addItemNm-1"
                        v-model="addInfo.addItemNm"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="addItemDetail-group-1"
                    label="상세설명:"
                    label-for="addItemDetail-1"
            >
                <b-form-textarea
                        id="textarea"
                        v-model="addInfo.addItemDetail"
                        rows="3"
                        max-rows="6"
                ></b-form-textarea>
            </b-form-group>
            <b-form-group
                    id="addItemNotice-group-1"
                    label="유의사항:"
                    label-for="addItemNotice-1"
            >
                <b-form-textarea
                        id="textarea"
                        v-model="addInfo.addItemNotice"
                        rows="3"
                        max-rows="6"
                ></b-form-textarea>
            </b-form-group>
            <b-container class="p-0">
                <b-row class="my-1">
                    <b-col sm="6">
                        <label class="mb-2 mr-sm-2 mb-sm-0" for="svcSellPrice-1">판매가</label>
                        <b-form-input
                                id="svcSellPrice-1"
                                v-model="addInfo.svcSellPrice"
                                type="text"
                                required
                        ></b-form-input>
                    </b-col>
                    <b-col sm="6">
                        <label class="mb-2 mr-sm-2 mb-sm-0" for="svcBasePrice-2">기본가격</label>
                        <b-form-input
                                id="svcBasePrice-2"
                                v-model="addInfo.svcBasePrice"
                                type="text"
                                required
                        ></b-form-input>
                    </b-col>
                </b-row>
            </b-container>
            <b-form-group
                    id="mbrNm-group-1"
                    label="이용기간:"
                    label-for="mbrNm-1"
            >
                <b-form-checkbox
                        id="checkbox-group-1"
                        v-model="addInfo.selected"
                        name="flavour-1"
                >이용 만료가 정해진 상품이면 체크 후 이용가능 기간을 설정하세요! U-CUBE 연동상품은 U-CUBE 상품정보 등록기간보다 길지 않아야 함.</b-form-checkbox>
                <b-row class="my-1">
                    <b-col sm="1">
                        <b-form-radio class="pr-1" v-model="addInfo.svcTermType" name="some-radios" value="H">시간</b-form-radio>
                    </b-col>
                    <b-col sm="3">
                        <b-form-input
                                id="svcSellPrice-1"
                                v-model="addInfo.svcTermUnit"
                                type="text"
                                required
                        ></b-form-input>
                    </b-col>
                    <b-col sm="1">
                        <b-form-radio v-model="addInfo.svcTermType" name="some-radios" value="D">일</b-form-radio>
                    </b-col>
                    <b-col sm="3">
                        <b-form-input
                                id="svcSellPrice-1"
                                v-model="addInfo.svcTermUnit"
                                type="text"
                                required
                        ></b-form-input>
                    </b-col>
                    <b-col sm="1">
                        <b-form-radio v-model="addInfo.svcTermType" name="some-radios" value="M">개월</b-form-radio>
                    </b-col>
                    <b-col sm="3">
                        <b-form-input
                                id="svcSellPrice-1"
                                v-model="addInfo.svcTermUnit"
                                type="text"
                                required
                        ></b-form-input>
                    </b-col>
                </b-row>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        name: "ModalAddInfo",

        props: ['selectedAddInfo', 'state'],

        data() {
            return {
                addInfo: {},
                authList: [],
                companyList: [],
                showMbrCompanyText : false
            };
        },

        async beforeMount() {
        },

        methods:{
            showModal() {

                this.$nextTick(async () => {
                    try {
                        if (this.$props.state === 'CREATE') {
                            this.addInfo = {};
                            return;
                        }

                        this.addInfo = {...this.$props.selectedAddInfo};
                    } catch (e) {

                    }
                });
            },

            async ok() {
                try {

                    const url = this.$props.state === 'CREATE' ? "/api/mbr/insertMbr" : "/api/mbr/updateMbr";
                    const addInfo = {...this.addInfo};



                    const response = await this.$axios.$post(url, addInfo);

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    this.$eventBus.$emit('refreshAddList')
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