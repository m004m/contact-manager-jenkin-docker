/*console.log("this is alert from js");*/

$('.carousel').carousel({
  interval: 2000
})


const togglesidebar=()=>{
  if($(".sidebar").is(":visible")){
    $(".sidebar").css("display","none");
    $(".content").css("margin-left","0%");

  }else{
    $(".sidebar").css("display","block");
    $(".content").css("margin-left","20%");
  }
}

setTimeout(() => {
  $(".msg").remove();
  $(".alert-success").remove();
  $(".alert-danger").remove();
}, "2000");

tinymce.init({
  selector: '#my-description'
});


function deleteContact(cId,currentPageNo){
	Swal.fire({
	  title: 'Are you sure?',
	  text: "You want delete this contact?",
	  icon: 'warning',
	  showCancelButton: true,
	  confirmButtonColor: '#3085d6',
	  cancelButtonColor: '#d33',
	  confirmButtonText: 'Yes, delete it!'
	}).then((result) => {
	  if (result.isConfirmed) {
		  
		  	window.location = "/user/show-conact/delete-contact/"+cId+"/"+currentPageNo;
		    Swal.fire(
		      'Deleted!',
		      'Your file has been deleted.',
		      'success'
		    )
	  }
	  
	})
}

