docker run -it `
  --cap-add=CHECKPOINT_RESTORE `
  --cap-add=SYS_PTRACE `
  --rm `
  -p 8081:8080 `
  --name petclinic_container `
  -v "${PWD}\crac-files:/opt/crac-files" `
  petclinic `
  java -XX:CRaCRestoreFrom=/opt/crac-files
