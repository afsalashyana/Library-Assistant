
# Set paths
$PROJECT_ROOT = "d:\lib\Library-Assistant"
$SRC_DIR = "$PROJECT_ROOT\src"
$LIB_DIR = "$PROJECT_ROOT\libs"
$BUILD_DIR = "$PROJECT_ROOT\build\classes"

$JAVA_EXE = "java"
$JAVAC_EXE = "javac"

# Create build directory
if (!(Test-Path $BUILD_DIR)) {
    New-Item -ItemType Directory -Force -Path $BUILD_DIR | Out-Null
}

# Construct classpath
# Note: specific order might matter - put jfxrt.jar first if possible
$JFXRT = "$LIB_DIR\jfxrt.jar"
if (Test-Path $JFXRT) {
    Write-Host "Found jfxrt.jar in libs."
    $CLASSPATH = "$JFXRT"
}
else {
    Write-Warning "jfxrt.jar NOT FOUND in libs. Compilation may fail."
    $CLASSPATH = ""
}

$LIBS = Get-ChildItem "$LIB_DIR\*.jar" | Where-Object { $_.Name -ne "jfxrt.jar" } | ForEach-Object { $_.FullName }
if ($CLASSPATH -ne "") {
    $CLASSPATH = "$CLASSPATH;" + ($LIBS -join ";") + ";$BUILD_DIR"
}
else {
    $CLASSPATH = ($LIBS -join ";") + ";$BUILD_DIR"
}

Write-Host "--- Debug Info ---"
Write-Host "CLASSPATH length: $($CLASSPATH.Length)"
Write-Host "------------------"

# Copy resources (FXML, CSS, images, etc.) to build directory
Write-Host "Copying resources..."
Copy-Item "$SRC_DIR\*" -Destination $BUILD_DIR -Recurse -Force -Exclude "*.java"

# Compile Java files
Write-Host "Compiling Java files..."
$JAVA_FILES = Get-ChildItem -Path $SRC_DIR -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }

# Write sources to file for compilation to avoid command line length limits
$JAVA_FILES | Out-File "$PROJECT_ROOT\sources.txt" -Encoding ASCII

# Compile
Write-Host "Running javac..."
& $JAVAC_EXE -d "$BUILD_DIR" -cp "$CLASSPATH" "@$PROJECT_ROOT\sources.txt"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful."
    Write-Host "Running application..."
    
    # Run application
    & $JAVA_EXE -cp "$CLASSPATH" library.assistant.ui.main.Main
}
else {
    Write-Host "Compilation failed."
    Write-Warning "If compilation failed due to JavaFX, ensure jfxrt.jar is in libs folder."
    Write-Warning "User confirmed they have it, checking again..."
    if (Test-Path $JFXRT) {
        Write-Host "CONFIRMED: jfxrt.jar IS in libs."
    }
}
