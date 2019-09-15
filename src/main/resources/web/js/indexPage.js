layui.use(["element"], ()=>{
   let element = layui.element;

   element.on("nav(test)", obj => {
      console.log(obj);
   });
});