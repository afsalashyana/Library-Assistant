
# Set paths
$PROJECT_ROOT = "d:\lib\Library-Assistant"
$SRC_DIR = "$PROJECT_ROOT\src"
$LIB_DIR = "$PROJECT_ROOT\libs"
$BUILD_DIR = "$PROJECT_ROOT\build\classes"
$DIST_DIR = "$PROJECT_ROOT\dist"
$VERSION = "1.0.0"

# Clean build and dist
if (Test-Path $BUILD_DIR) { Remove-Item -Recurse -Force $BUILD_DIR }
if (Test-Path $DIST_DIR) { Remove-Item -Recurse -Force $DIST_DIR }
New-Item -ItemType Directory -Force -Path $BUILD_DIR | Out-Null
New-Item -ItemType Directory -Force -Path $DIST_DIR | Out-Null

# Construct classpath for compilation
$JFXRT = "$LIB_DIR\jfxrt.jar"
$LIBS = Get-ChildItem "$LIB_DIR\*.jar" | Where-Object { $_.Name -ne "jfxrt.jar" } | ForEach-Object { $_.FullName }
$CLASSPATH = "$JFXRT;" + ($LIBS -join ";") + ";$BUILD_DIR"

# Copy resources
Write-Host "Copying resources..."
Copy-Item "$SRC_DIR\*" -Destination $BUILD_DIR -Recurse -Force -Exclude "*.java"

# Compile
Write-Host "Compiling..."
$JAVA_FILES = Get-ChildItem -Path $SRC_DIR -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
$JAVA_FILES | Out-File "$PROJECT_ROOT\sources.txt" -Encoding ASCII
& javac -d "$BUILD_DIR" -cp "$CLASSPATH" "@$PROJECT_ROOT\sources.txt"

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed!"
    exit 1
}

# Create Manifest
$MANIFEST_PATH = "$PROJECT_ROOT\manifest.mf"
$MANIFEST_CONTENT = "Manifest-Version: 1.0`nMain-Class: library.assistant.ui.main.Main`nClass-Path: "
# Add libs to Class-Path in manifest (relative path)
$LIB_NAMES = Get-ChildItem "$LIB_DIR\*.jar" | ForEach-Object { "libs/" + $_.Name }
$MANIFEST_CONTENT += ($LIB_NAMES -join " ") + "`n"
$MANIFEST_CONTENT | Out-File $MANIFEST_PATH -Encoding ASCII

# Create JAR
Write-Host "Creating JAR..."
$JAR_NAME = "LibraryAssistant.jar"
& jar camf "$MANIFEST_PATH" "$DIST_DIR\$JAR_NAME" -C "$BUILD_DIR" .

# Package into ZIP
Write-Host "Packaging into ZIP..."
$ZIP_NAME = "LibraryAssistant-v$VERSION.zip"
$ZIP_PATH = "$DIST_DIR\$ZIP_NAME"

# Create a temporary folder for zipping
$TEMP_ZIP_DIR = "$DIST_DIR\LibraryAssistant"
New-Item -ItemType Directory -Force -Path $TEMP_ZIP_DIR | Out-Null

# Copy JAR and Libs
Copy-Item "$DIST_DIR\$JAR_NAME" -Destination $TEMP_ZIP_DIR
New-Item -ItemType Directory -Force -Path "$TEMP_ZIP_DIR\libs" | Out-Null
Copy-Item "$LIB_DIR\*" -Destination "$TEMP_ZIP_DIR\libs" -Recurse

# Zip it
Compress-Archive -Path "$TEMP_ZIP_DIR\*" -DestinationPath $ZIP_PATH -Force

# Cleanup temp
Remove-Item -Recurse -Force $TEMP_ZIP_DIR

Write-Host "Build complete: $ZIP_PATH"
