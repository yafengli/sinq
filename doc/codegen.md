## Build from the source
+ 使用`TableProc`参数

        import io.sinq.codegen.TableProc
        val tableProc = new TableProc("scan.jpa.entity.package", "out.object.package", TypeDataMap.HIBERNATE)
        tableProc.proc()
