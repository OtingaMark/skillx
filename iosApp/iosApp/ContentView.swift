import SwiftUI
import shared

/**
 * Root SwiftUI view that bridges to Compose Multiplatform shared UI.
 * Uses UIViewControllerRepresentable to embed the shared Compose navigation graph.
 */
struct ContentView: View {
    var body: some View {
        ComposeViewControllerBridge()
            .ignoresSafeArea()
    }
}

/**
 * Bridges the shared Compose Multiplatform SkillXNavGraph to SwiftUI.
 * Creates a UIViewController that hosts the Compose UI.
 */
private struct ComposeViewControllerBridge: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}