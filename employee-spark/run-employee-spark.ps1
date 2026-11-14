
$env:HADOOP_HOME = "C:\hadoop"
$env:PATH = "$env:HADOOP_HOME\bin;$env:PATH"

if (-not (Test-Path "$env:HADOOP_HOME\bin\winutils.exe")) {
    Write-Error "winutils.exe was not found."
    exit 1
}

$jar = ".\target\scala-2.12\employee-spark-assembly-0.1.0.jar"

if (-not (Test-Path $jar)) {
    Write-Error "Assembly JAR not found. Run sbt clean test assembly."
    exit 1
}

Write-Host "Starting Employee Analysis with spark-submit..."

spark-submit `
    --class com.training.EmployeeAnalysis `
    --master "local[*]" `
    $jar

if ($LASTEXITCODE -ne 0) {
    Write-Error "Spark application failed."
    exit $LASTEXITCODE
}

Write-Host "Employee Analysis completed successfully."
