<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_addInfo"
            @ok.prevent="submit"
            ref="modal"
            @show="showModal"
            centered
            :title="title">
        <b-form>
            <b-form-group
                    id="addItemNm-group-1"
                    label="상품구분:"
                    label-for="addItemNm-1"
                    :class="'mt-1'"
            >
                <b-form-radio-group
                        @change="changeAddItemType"
                        v-model="addInfo.addItemType"
                >
                    <b-row class="my-1">
                        <b-col sm="5">
                            <b-form-radio v-if="state === 'CREATE' || state === 'UPDATE' && addInfo.addItemType === 'U'" value="U">U-Cube 연동 상품</b-form-radio>
                            <b-form-radio v-if="state === 'CREATE' || state === 'UPDATE' && addInfo.addItemType === 'G'" value="G">NVIDIA 직업 연동 상품</b-form-radio>
                        </b-col>
                        <b-col sm="7">
                            <b-form-input
                                    id="addItemCode-1"
                                    v-model="addInfo.addItemCode"
                                    :disabled="disabledAddItemCode"
                                    v-validate="'required'"
                                    data-vv-name="부가서비스코드"
                            ></b-form-input>
                            <b-form-invalid-feedback :state="!errors.has('부가서비스코드')">
                                {{errors.first('부가서비스코드')}}
                            </b-form-invalid-feedback>
                        </b-col>
                    </b-row>

                </b-form-radio-group>
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
                            name="addItemNm"
                            v-validate="'required'"
                            data-vv-name="부가서비스명"
                    ></b-form-input>
                    <b-form-invalid-feedback :state="!errors.has('부가서비스명')">
                        {{errors.first('부가서비스명')}}
                    </b-form-invalid-feedback>

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
                                :disabled="disabledSvcSellPrice"
                                type="number"
                                v-validate="'required'"
                                data-vv-name="판매가"
                        ></b-form-input>
                        <b-form-invalid-feedback :state="!errors.has('판매가')">
                            {{errors.first('판매가')}}
                        </b-form-invalid-feedback>
                    </b-col>
                    <b-col sm="6">
                        <label class="mb-2 mr-sm-2 mb-sm-0" for="svcBasePrice-2">기본가격</label>
                        <b-form-input
                                id="svcBasePrice-2"
                                v-model="addInfo.svcBasePrice"
                                type="number"
                                :disabled="disabledSvcBasePrice"
                                v-validate="'required'"
                                data-vv-name="기본가격"
                        ></b-form-input>
                        <b-form-invalid-feedback :state="!errors.has('기본가격')">
                            {{errors.first('기본가격')}}
                        </b-form-invalid-feedback>
                    </b-col>
                </b-row>
            </b-container>
            <b-form-group
                    id="svcTermType-group-1"
                    label="이용기간 유무:"
                    label-for="svcTermType-1"
            >
                <b-form-radio-group v-model="addInfo.svcTermType" @change="changeSvcTermType">
                    <b-form-radio v-if="addInfo.addItemType === 'U'" value="0">없음</b-form-radio>
                    <b-form-radio v-if="addInfo.addItemType === 'G'" value="1">이용기간</b-form-radio>
                    <b-form-radio v-if="addInfo.addItemType === 'G'" value="2">기간한정</b-form-radio>
                </b-form-radio-group>
            </b-form-group>
            <b-form-group
                    id="mbrNm-group-1"
                    label="이용 만료가 정해진 상품이면 체크 후 이용가능 기간을 설정하세요! U-CUBE 연동상품은 U-CUBE 상품정보 등록기간보다 길지 않아야 함."
                    label-for="mbrNm-1"
                    v-if="addInfo.svcTermType === '1'"
            >
                <b-form-radio-group v-model="addInfo.svcTermUnit" @change="changeSvcTermUnit">
                    <b-row class="my-1">
                        <b-col sm="1">
                            <b-form-radio value="H">시간</b-form-radio>
                        </b-col>
                        <b-col sm="3">
                            <b-form-input
                                    id="svcTermNum-1"
                                    v-model="addInfo.svcTermNumHour"
                                    type="number"
                                    :disabled="addInfo.svcTermUnit !== 'H'"
                                    v-validate="'required'"
                                    data-vv-name="시간"
                            ></b-form-input>
                            <b-form-invalid-feedback :state="!errors.has('시간')">
                                {{errors.first('시간')}}
                            </b-form-invalid-feedback>
                        </b-col>
                        <b-col sm="1">
                            <b-form-radio value="D">일</b-form-radio>
                        </b-col>
                        <b-col sm="3">
                            <b-form-input
                                    id="svcTermNum-1"
                                    v-model="addInfo.svcTermNumDay"
                                    type="number"
                                    :disabled="addInfo.svcTermUnit !== 'D'"
                                    v-validate="'required'"
                                    data-vv-name="일"
                            ></b-form-input>
                            <b-form-invalid-feedback :state="!errors.has('일')">
                                {{errors.first('일')}}
                            </b-form-invalid-feedback>
                        </b-col>
                        <b-col sm="1">
                            <b-form-radio value="M">개월</b-form-radio>
                        </b-col>
                        <b-col sm="3">
                            <b-form-input
                                    id="svcTermNum-1"
                                    v-model="addInfo.svcTermNumMonth"
                                    type="number"
                                    :disabled="addInfo.svcTermUnit !== 'M'"
                                    v-validate="'required'"
                                    data-vv-name="개월"
                            ></b-form-input>
                            <b-form-invalid-feedback :state="!errors.has('개월')">
                                {{errors.first('개월')}}
                            </b-form-invalid-feedback>
                        </b-col>
                    </b-row>
                </b-form-radio-group>
            </b-form-group>
            <b-form-group
                    v-if="addInfo.svcTermType === '2'"
                    id="svcTermDate-group-1"
                    label="이용종료일시:"
                    label-for="svcTermDate-1"
            >
                    <b-form-input id="svcTermDate-1"
                                  v-model="addInfo.svcTermDate"
                                  type="date"
                                  v-validate="'required'"
                                  data-vv-name="이용종료일시"
                    ></b-form-input>
                <b-form-invalid-feedback :state="!errors.has('이용종료일시')">
                    {{errors.first('이용종료일시')}}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                    id="useYn-group-1"
                    label="사용여부:"
                    label-for="useYn-1"
            >
                <b-form-select
                        id="statusCd-1"
                        :required="true"
                        v-model="addInfo.statusCd"
                        v-validate="'required'"
                        data-vv-name="사용여부"
                >
                    <option value="0">미사용</option>
                    <option value="1">사용</option>
                </b-form-select>
                <b-form-invalid-feedback :state="!errors.has('사용여부')">
                    {{errors.first('사용여부')}}
                </b-form-invalid-feedback>
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
                addItemOptions :[
                    {text : 'U-Cube 연동 상품', value: 'U'},
                    {text : 'NVIDIA 직업 연동 상품', value: 'G'}
                ],
                disabledSvcBasePrice: false,
                disabledSvcSellPrice: false,
                disabledAddItemCode: false,
                title: ''
            };
        },

        async beforeMount() {
        },

        methods:{
            showModal() {

                this.$nextTick(async () => {
                    try {
                        this.errors.clear();

                        if (this.$props.state === 'CREATE') {
                            console.log('this.$props.state', this.$props.state);
                            console.log('this.addInfo', this.addInfo);
                            this.disabledAddItemCode = false;
                            this.addInfo = { addItemType : 'U', svcTermType : '0', svcTermUnit: 'H'};
                            this.title = '부가서비스정보 등록';
                            return;
                        }

                        const addItemCode = this.$props.selectedAddInfo.addItemCode;
                        this.disabledAddItemCode = true;
                        this.addInfo = await this.getAddInfo(addItemCode);
                        this.title = '부가서비스정보 수정';

                        console.log('this.addInfo ', this.addInfo );
                        if(this.addInfo.svcTermType === '1'){
                            switch (this.addInfo.svcTermUnit) {
                                case 'M':
                                    this.$set(this.addInfo, 'svcTermNumMonth',  this.addInfo.svcTermNum);
                                    break;
                                case 'D':
                                    this.$set(this.addInfo, 'svcTermNumDay',  this.addInfo.svcTermNum);
                                    break;
                                case 'H':
                                    this.$set(this.addInfo, 'svcTermNumHour',  this.addInfo.svcTermNum);
                                    break;
                            }
                        }

                        if (this.addInfo.addItemType === 'G') {
                            this.disabledSvcSellPrice = true;
                            this.disabledSvcBasePrice = true;
                        } else{
                            this.disabledSvcSellPrice = false;
                            this.disabledSvcBasePrice = false;
                        }

                        if(this.addInfo.svcTermDate){
                            this.$set(this.addInfo, 'svcTermDate',  this.addInfo.svcTermDate.substring(0,10));
                        }

                    } catch (e) {
                        console.dir(e);
                        e = (e.response && e.response.data) || e;

                        await this.$bvModal.msgBoxOk(e.message);
                    }
                });
            },

            async submit() {
                if(!await this.$validator.validate()) {
                    console.dir(this.$validator);
                    return;
                }
                await this.ok();
            },

            async ok() {
                try {

                    const url = this.$props.state === 'CREATE' ? "/add/insertAddItem" : "/add/updateAddItem";
                    const addInfo = {...this.addInfo};

                    if(addInfo.svcTermType === '1'){
                        switch (addInfo.svcTermUnit) {
                            case 'M':
                                addInfo.svcTermNum = addInfo.svcTermNumMonth;
                                break;
                            case 'D':
                                addInfo.svcTermNum = addInfo.svcTermNumDay;
                                break;
                            case 'H':
                                addInfo.svcTermNum = addInfo.svcTermNumHour;
                                break;
                        }

                        // 기간한정의 날짜 초기화
                        addInfo.svcTermDate = '';
                    }

                    const response = await this.$axios.$post(process.env.contextPath + url, addInfo);

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    this.$eventBus.$emit('refreshAddList')
                    this.$refs.modal.hide();
                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }

            },

            changeAddItemType(addItemType) {
                console.log('addItemType', addItemType);
                if (addItemType === 'U') {
                    this.$set(this.addInfo, 'svcBasePrice', '0');
                    this.$set(this.addInfo, 'svcSellPrice', '0');
                    this.$set(this.addInfo, 'svcTermType', '0');
                    this.disabledSvcBasePrice = false
                    this.disabledSvcSellPrice = false;
                    if(this.$props.state === 'CREATE')  this.disabledAddItemCode = false;
                } else {
                    this.$set(this.addInfo, 'svcBasePrice', '0');
                    this.$set(this.addInfo, 'svcSellPrice', '0');
                    this.$set(this.addInfo, 'svcTermType', '2');

                    this.disabledSvcSellPrice = true;
                    this.disabledSvcBasePrice = true;
                    if(this.$props.state === 'CREATE')  {
                        this.disabledAddItemCode = true;
                        this.$set(this.addInfo, 'addItemCode', '');
                    }
                }

                this.errors.clear();
            },

            changeSvcTermType() {
                if (!this.addInfo.svcTermUnit) {
                    this.addInfo.svcTermUnit = 'H';
                }
            },

            changeSvcTermUnit() {
                this.$set(this.addInfo, 'svcTermNumHour', '');
                this.$set(this.addInfo, 'svcTermNumDay', '');
                this.$set(this.addInfo, 'svcTermNumMonth', '');
            },

            async getAddInfo(addItemCode) {
                try {
                    const {data} = await this.$axios.post(process.env.contextPath + '/add/selectAddItem', {
                        addItemCode
                    });
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