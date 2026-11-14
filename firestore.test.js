const { initializeTestEnvironment, assertFails, assertSucceeds } = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

const PROJECT_ID = "demo-no-project";
const DATABASE_ID = "ai-studio-android-joyverse-fea37609-bc64-4ae1-93d2-ad2cdcb18260";

let testEnv;

before(async () => {
  const rules = fs.readFileSync("firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: "127.0.0.1",
      port: 8085
    }
  });
});

beforeEach(async () => {
  await testEnv.clearFirestore();
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

test("Unauthenticated user cannot read profiles", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore(DATABASE_ID);
  await assertFails(unauthDb.collection("users").doc("alice").get());
});

test("Authenticated user can create and read their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext("alice").firestore(DATABASE_ID);
  const now = new Date();
  await assertSucceeds(aliceDb.collection("users").doc("alice").set({
    userId: "alice",
    username: "alice_star",
    avatar: "cosmic_avatar_1",
    bio: "Exploring the joyverse",
    interests: ["Coding", "Astronomy"],
    favoriteCategories: ["Coding"],
    personalGoals: ["Learn daily"],
    preferredLanguages: ["English"],
    recommendationPrefs: ["Creative"],
    xp: 0,
    level: 1,
    streak: 0,
    createdAt: now,
    updatedAt: now
  }));

  const doc = await assertSucceeds(aliceDb.collection("users").doc("alice").get());
  if (!doc.exists) throw new Error("Document should exist");
});

test("User cannot modify another user's profile", async () => {
  const bobDb = testEnv.authenticatedContext("bob").firestore(DATABASE_ID);
  const now = new Date();
  await assertFails(bobDb.collection("users").doc("alice").set({
    userId: "alice",
    username: "imposter",
    avatar: "cosmic_avatar_1",
    xp: 100,
    level: 2,
    streak: 5,
    createdAt: now,
    updatedAt: now
  }));
});

test("User can manage own activities, but not another user's activities", async () => {
  const aliceDb = testEnv.authenticatedContext("alice").firestore(DATABASE_ID);
  const bobDb = testEnv.authenticatedContext("bob").firestore(DATABASE_ID);
  const now = new Date();

  await assertSucceeds(aliceDb.collection("users").doc("alice").collection("activities").doc("act_1").set({
    userId: "alice",
    title: "Morning Coding",
    category: "Coding",
    estimatedMinutes: 30,
    isCompleted: false,
    createdAt: now,
    updatedAt: now
  }));

  // Bob cannot read Alice's activity
  await assertFails(bobDb.collection("users").doc("alice").collection("activities").doc("act_1").get());

  // Bob cannot write to Alice's activities
  await assertFails(bobDb.collection("users").doc("alice").collection("activities").doc("act_2").set({
    userId: "alice",
    title: "Infiltrate",
    category: "Hacking",
    estimatedMinutes: 10,
    isCompleted: false,
    createdAt: now,
    updatedAt: now
  }));
});

test("Community posts can only be edited/deleted by their author", async () => {
  const aliceDb = testEnv.authenticatedContext("alice").firestore(DATABASE_ID);
  const bobDb = testEnv.authenticatedContext("bob").firestore(DATABASE_ID);
  const now = new Date();

  await assertSucceeds(aliceDb.collection("posts").doc("post_1").set({
    authorId: "alice",
    authorUsername: "alice_star",
    title: "First Post",
    content: "Welcome to Joyverse!",
    category: "General",
    createdAt: now
  }));

  // Bob can read the post
  await assertSucceeds(bobDb.collection("posts").doc("post_1").get());

  // Bob cannot edit Alice's post
  await assertFails(bobDb.collection("posts").doc("post_1").update({
    content: "Hacked by Bob"
  }));

  // Alice can delete her post
  await assertSucceeds(aliceDb.collection("posts").doc("post_1").delete());
});
