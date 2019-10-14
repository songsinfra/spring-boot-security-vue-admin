<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_entrInfo"
            @ok.prevent="ok"
            ref="modal"
            @show="showModal"
            centered
            :title="title">
        <b-form>
            <b-form-group
                    id="useYn-group-1"
                    label="SOC타입코드:"
                    label-for="useYn-1"
            >
                <b-form-select
                        id="socTypeCode-1"
                        :required="true"
                        v-model="entrInfo.socTypeCode"
                >
                    <option :value="undefined">--- SOC타입코드을 선택하세요 ---</option>
                    <option value="P">요금제</option>
                    <option value="R">부가서비스</option>
                </b-form-select>
            </b-form-group>
            <b-form-group
                    id="prodCd-group-1"
                    label="서비스코드:"
                    label-for="prodCd-1"
            >
                <b-form-select
                        id="prodCd-1"
                        :required="true"
                        v-model="entrInfo.prodCd"
                >
                    <option :value="undefined">--- 서비스코드를 선택하세요 ---</option>
                    <option value="LZP0000001">모바일</option>
                    <option value="LZP0000601">인터넷</option>
                    <option value="LZP0000701">IPTV</option>
                </b-form-select>
            </b-form-group>
            <b-container class="p-0">
                <b-row class="my-1">
                    <b-col sm="6">
                        <label class="mb-2 mr-sm-2 mb-sm-0" for="entrItemNm-1">요금제명</label>
                        <b-form-input
                                id="entrItemNm-1"
                                v-model="entrInfo.entrItemNm"
                                required
                        ></b-form-input>
                    </b-col>
                    <b-col sm="6">
                        <label class="mb-2 mr-sm-2 mb-sm-0" for="entrItemCode-2">코드</label>
                        <b-form-input
                                id="entrItemCode-2"
                                v-model="entrInfo.entrItemCode"
                                required
                        ></b-form-input>
                    </b-col>
                </b-row>
            </b-container>
            <b-form-group
                    id="memo-group-1"
                    label="메모:"
                    label-for="memo-1"
            >
                <b-form-textarea
                        id="textarea"
                        v-model="entrInfo.memo"
                        rows="3"
                        max-rows="6"
                ></b-form-textarea>
            </b-form-group>
            <b-form-group
                    id="useYn-group-1"
                    label="사용여부:"
                    label-for="useYn-1"
            >
                <b-form-select
                        id="statusCd-1"
                        :required="true"
                        v-model="entrInfo.statusCd"
                >
                    <option value="0">미사용</option>
                    <option value="1">사용</option>
                </b-form-select>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        name: "ModalEntrInfo",

        props: ['selectedEntrInfo', 'state'],

        data() {
            return {
                entrInfo: {},
                title: ''
            };
        },

        methods:{
            showModal() {
                this.$nextTick(async () => {
                    try {
                        if (this.$props.state === 'CREATE') {
                            this.entrInfo = { statusCd : '1'};
                            this.title = '권한정보 등록';
                            return;
                        }

                        console.log('this.$props.selectedEntrInfo', this.$props.selectedEntrInfo);
                        this.title = '권한정보 수정';
                        this.entrInfo = {...this.$props.selectedEntrInfo};
                    } catch (e) {

                    }
                });
            },

            async ok() {
                try {

                    const url = this.$props.state === 'CREATE' ? "/api/entr/insertEntrItem" : "/api/entr/updateEntrItem";
                    const entrInfo = {...this.entrInfo};

                    const response = await this.$axios.$post(url, entrInfo);

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    this.$eventBus.$emit('refreshEntrList')
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