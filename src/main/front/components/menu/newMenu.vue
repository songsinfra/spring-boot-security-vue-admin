<template>
    <b-modal id="modal_update_menu"
             @ok="ok"
             ref="modal"
             @show="showModal"
             centered
             title="메뉴 수정">
        <b-form>
            <b-form-group
                    id="upMenuId-group-1"
                    label="상위메뉴:"
                    label-for="upMenuId-1"
            >
                <b-form-input
                        id="upMenuId-1"
                        v-model="menuInfo.upMenuId"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="menuName-group-1"
                    label="메뉴명:"
                    label-for="menuName-1"
                    description="We'll never share your email with anyone else."
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
                <b-form-input
                        id="useYn-1"
                        v-model="menuInfo.useYn"
                        required
                ></b-form-input>
            </b-form-group>
            <b-form-group
                    id="input-group-1"
                    label="URL:"
                    label-for="menuURL-1"
            >
                <b-form-input
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
        name: "newMenu",
        props: ['selectedMenu', 'state'],
        data(){
            return {
                menuInfo: {}
            }
        },
        watch: {
            selectedMenu() {
                console.log("change selectedMenu");
            }
        },
        methods:{
            async ok(bvModalEvt) {
                try {
                    bvModalEvt.preventDefault();

                    const url = this.$props.state === 'CREATE' ? "/api/menu/setMenuAdd" : "/api/menu/setMenuUpdate";

                    const response = await this.$axios.$post(url, {
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

                    this.$bvModal.msgBoxOk(e.message);
                }
            },
            async showModal() {
                try {
                    this.$nextTick(async () => {
                        if(!this.$props.selectedMenu.menuId) return;

                        const menuId = this.$props.selectedMenu.menuId;
                        const response = await this.$axios.$post('/api/menu/getMenuInfo', {
                            menuId: menuId
                        });
                        this.menuInfo = response;
                    })

                } catch (e) {
                    this.$bvModal.msgBoxOk(e.message);
                }
            }
        }
    }
</script>

<style scoped>

</style>