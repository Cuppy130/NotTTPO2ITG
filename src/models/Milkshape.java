package models;

import static org.lwjgl.assimp.Assimp.*;
import static org.lwjgl.opengl.GL30.*;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Milkshape {
    private AIScene scene;
    private List<Float> vertices = new ArrayList<>();
    private List<Float> normals = new ArrayList<>();
    private List<Float> texCoords = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    public Milkshape(String filepath) {
        loadMilkshapeModel(filepath);
    }

    private void loadMilkshapeModel(String filepath) {
        this.scene = aiImportFile(filepath, aiProcess_Triangulate | aiProcess_FlipUVs);
        
        if (scene == null) {
            throw new RuntimeException("Error loading Milkshape model: " + aiGetErrorString());
        }

        processScene(scene);
    }

    private void processScene(AIScene scene) {
        for (int i = 0; i < scene.mNumMeshes(); i++) {
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));
            processMesh(mesh);
        }
    }


    private void processMesh(AIMesh mesh) {
        for (int i = 0; i < mesh.mNumVertices(); i++) {
            AIVector3D vertex = mesh.mVertices().get(i);
            vertices.add(vertex.x());
            vertices.add(vertex.y());
            vertices.add(vertex.z());
    
            if (mesh.mNormals() != null) {
                AIVector3D normal = mesh.mNormals().get(i);
                normals.add(normal.x());
                normals.add(normal.y());
                normals.add(normal.z());
            }
    
            if (mesh.mTextureCoords(0) != null) {
                AIVector3D texCoord = mesh.mTextureCoords(0).get(i);
                texCoords.add(texCoord.x());
                texCoords.add(texCoord.y());
            }
        }
    
        for (int i = 0; i < mesh.mNumFaces(); i++) {
            AIFace face = mesh.mFaces().get(i);
            for (int j = 0; j < face.mNumIndices(); j++) {
                indices.add(face.mIndices().get(j));
            }
        }
    }
    
    private int createVBO(FloatBuffer buffer) {
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        return vboId;
    }
    
    private int createEBO(IntBuffer buffer) {
        int eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        return eboId;
    }
    
    public void createBuffers() {
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.size());
        vertices.forEach(verticesBuffer::put);
        verticesBuffer.flip();
        int vbo = createVBO(verticesBuffer);
    
        IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.size());
        indices.forEach(indicesBuffer::put);
        indicesBuffer.flip();
        int ebo = createEBO(indicesBuffer);
    
        // Repeat for normals and texture coordinates if needed
        MemoryUtil.memFree(verticesBuffer);
        MemoryUtil.memFree(indicesBuffer);
    }
    

    public void cleanup() {
        aiReleaseImport(scene); // Free up resources
    }
}
