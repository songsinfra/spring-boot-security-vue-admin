<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
    <b-modal
            id="modal_update_menu"
            @ok.prevent="ok"
            ref="modal"
            @show="showModal"
            centered
            :title="title">
        <b-form>
            <b-form-group
                    id="upMenuId-group-1"
                    label="상위메뉴:"
                    label-for="upMenuId-1"
            >
                <b-form-select
                        v-model="menuInfo.upMenuId"
                       :options="upMenuIdOption"
                       :value-field="'menuId'"
                       :text-field="'menuName'"
                       :disabled="state !=='CREATE'"
                       :class="'form-control'"
                >
                    <template v-slot:first>
                        <option
                                :value="undefined"
                                disabled
                        >-- 선택하세요 --</option>
                    </template>
                </b-form-select>
            </b-form-group>
            <b-form-group
                    id="menuName-group-1"
                    label="메뉴명:"
                    label-for="menuName-1"
                    :class="'mt-1'"
            >
                <b-form-input
                        id="menuName-1"
                        v-model="menuInfo.menuName"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="ordNo-group-1"
                    label="정렬값:"
                    label-for="ordNo-1"
            >
                <b-form-input
                        id="ordNo-1"
                        v-model="menuInfo.ordNo"
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
                        v-model="menuInfo.useYn"
                        :options="useYnOption"
                ></b-form-select>
            </b-form-group>
            <b-form-group
                    id="input-group-1"
                    label="URL:"
                    label-for="menuURL-1"
            >
                <b-form-input
                        :disabled="state !=='CREATE'"
                        id="menuURL-1"
                        v-model="menuInfo.menuURL"
                        required
                ></b-form-input>
            </b-form-group>
        </b-form>
    </b-modal>
</template>

<script>
    export default {
        name: "modalMenu",
        props: ['selectedMenu', 'state'],
        data(){
            return {
                menuInfo: {},
                selectedUpMenuId: null,
                title: ''
            }
        },
        computed: {
            upMenuIdOption() {
                return this.$store.state.menu.upMenuList;
            },
            useYnOption() {
                return this.$store.state.menu.useYnList;
            }
        },

        methods:{
            async ok(bvModalEvt) {
                try {
                    bvModalEvt.preventDefault();

                    const url = this.$props.state === 'CREATE' ? "/menu/setMenuAdd" : "/menu/setMenuUpdate";

                    const response = await this.$axios.$post(process.env.contextPath + url, {
                        upMenuId: this.menuInfo.upMenuId,
                        menuId: this.menuInfo.menuId,
                        menuName: this.menuInfo.menuName,
                        menuURL: this.menuInfo.menuURL,
                        ordNo: this.menuInfo.ordNo,
                        useYn: this.menuInfo.useYn
                    });

                    await this.$bvModal.msgBoxOk("저장이 완료 되었습니다.");
                    this.$eventBus.$emit('refreshGrid')
                    this.$refs.modal.hide();
                } catch (e) {
                    console.dir(e);
                    e = (e.response && e.response.data) || e;

                    await this.$bvModal.msgBoxOk(e.message);
                }
            },
            async showModal() {
                this.$nextTick(async () => {
                    try {
                        if (this.$props.state === 'CREATE') {
                            this.menuInfo = {};
                            this.title = '메뉴 등록';
                            return;
                        }

                        const menuId = this.$props.selectedMenu.menuId;
                        const response = await this.$axios.$post(process.env.contextPath + '/menu/getMenuInfo', {
                            menuId: menuId
                        });
                        this.menuInfo = response;
                        this.title = '메뉴 수정';
                    } catch (e) {
                        const status = e.response && e.response.status;
                        console.log('error-------------', status);
                        if (status === 401) {
                            this.$router.push("/login/login");
                        } else {
                            await this.$bvModal.msgBoxOk(e.message);
                        }
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>